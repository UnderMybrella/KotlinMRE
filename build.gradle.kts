import org.gradle.kotlin.dsl.support.unzipTo
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.ExecutionTaskHolder
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import org.jetbrains.kotlin.gradle.testing.KotlinAggregatingTestRun

plugins {
    val kotlinVersion = System.getenv("KOTLIN_VERSION") ?: "1.8.21"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false

    id("io.kotest.multiplatform") version "5.6.2" apply false
}

val platform = System.getProperty("os.name").filterNot(Char::isWhitespace)
val kotlinVersion = System.getenv("KOTLIN_VERSION") ?: "1.8.21"
val serializationVersion = System.getenv("SERIALIZATION_VERSION") ?: "1.5.1"

val reportBinariesDir = layout.buildDirectory.dir("binaryResults")
val zipBinaryResults = tasks.register<Zip>("zipBinaryResults") {
    destinationDirectory.set(reportBinariesDir)
    archiveBaseName.set("${platform}_${kotlinVersion.replace('.', '-')}_${serializationVersion.replace('.', '-')}")

    group = "report"
}
val groupReportFromZips = tasks.register<TestReport>("groupReportFromZips") {
    val zips = reportBinariesDir.map { binaries ->
        binaries.asFileTree.matching { include("*.zip") }
            .map { zip -> binaries.dir(zip.nameWithoutExtension) to zip }
    }

    destinationDirectory.set(layout.buildDirectory.dir("groupReport"))
    testResults.from(zips.map { it.flatMap { (dir) -> dir.asFile.walk().filter { File(it, "results.bin").exists() } } })
    outputs.upToDateWhen { false }

    doFirst {
        zips.get().forEach { (dir, zip) -> unzipTo(dir.asFile, zip) }
        println(testResults.files)
    }

    group = "report"
}

allprojects {
    group = "dev.brella"

    repositories {
        mavenCentral()
    }
}

subprojects {
    val project = this
    val allTests = tasks.withType<AbstractTestTask>()

    allTests.configureEach {
        ignoreFailures = true
    }

    allTests.withType<Test>()
        .configureEach {
            useJUnitPlatform()
        }

    val defaultIncludePlatform = true
    val defaultIncludeKotlinVersion = true
    val defaultIncludeSerializationVersion = false

    fun getNameFor(
        target: String?,
        includePlatform: Boolean = defaultIncludePlatform,
        includeKotlinVersion: Boolean = defaultIncludeKotlinVersion,
        includeSerializationVersion: Boolean = defaultIncludeSerializationVersion,
    ): String = ArrayList<String>().apply {
        target?.let { add(it) }
        if (includePlatform) {
            add("platform = $platform")
        }
        if (includeKotlinVersion) {
            add("kotlin = $kotlinVersion")
        }
        if (includeSerializationVersion) {
            add("kotlinx.serialization = $serializationVersion")
        }
    }.joinToString()

    afterEvaluate {
        val mpp = extensions.findByType<KotlinMultiplatformExtension>()

        println("<== $name ==>")

        allTests.withType<KotlinTest>()
            .configureEach {
                targetName = getNameFor(
                    targetName,
                    includeSerializationVersion = mpp?.doesTestTargetHaveDependency(
                        name,
                        "kotlinx-serialization"
                    ) == true
                )
            }

        allTests.withType<KotlinJvmTest>()
            .configureEach {
                targetName = getNameFor(
                    targetName,
                    includeSerializationVersion = mpp?.doesTestTargetHaveDependency(
                        name,
                        "kotlinx-serialization"
                    ) == true
                )
            }
    }

    zipBinaryResults.configure {
        val zip = this

        allTests.forEach { test ->
            zip.from(test.binaryResultsDirectory) {
                rename { "${project.path.trimStart(':').replace(':', '/')}/${test.name}/$it" }
            }
        }
    }
}

fun KotlinMultiplatformExtension.doesTestTargetHaveDependency(testName: String, dependencyName: String): Boolean {
    val target = testableTargets.firstOrNull { target ->
//                    println("<==== ${target.targetName} ====>")
        val testRuns = target.testRuns.flatMap {
            if (it is KotlinAggregatingTestRun<*, *, *>) it.getConfiguredExecutions() else listOf(it)
        }

        testRuns
            .filterIsInstance<ExecutionTaskHolder<*>>()
            .any { it.executionTask.name == testName }
    } ?: return false

    val testCompilation = target.compilations.findByName("test") ?: return false
    val configuration =
        target.project.configurations.findByName(testCompilation.compileDependencyConfigurationName)?.resolvedConfiguration
            ?: return false

    return configuration.resolvedArtifacts.any { it.name.contains(dependencyName) }
}