plugins {
    kotlin("multiplatform")
}

group = "dev.brella"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native") {
            binaries.executable()
        }

        hostOs == "Linux" -> linuxX64("native") {
            binaries.executable()
        }

        isMingwX64 -> mingwX64("native") {
            binaries.executable()
        }

        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("reflect"))
                api("io.kotest:kotest-framework-engine:5.6.2")
                api("io.kotest:kotest-assertions-core:5.6.2")
            }
        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                api("io.kotest:kotest-runner-junit5:5.6.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting

        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}