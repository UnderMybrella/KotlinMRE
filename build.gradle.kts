plugins {
    kotlin("jvm") version "1.8.20" apply false
    kotlin("multiplatform") version "1.8.20" apply false
    id("io.kotest.multiplatform") version "5.6.2" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
}

allprojects {
    group = "dev.brella"

    repositories {
        mavenCentral()
    }
}