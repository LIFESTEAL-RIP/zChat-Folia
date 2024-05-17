plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.mrzcookie"
version = "1.0.3"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("net.kyori:adventure-text-serializer-plain:4.16.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.16.0")

    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    assemble {
        dependsOn(shadowJar)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        filesMatching("plugin.yml") {
            expand(
                    "version" to project.version
            )
        }
        filesMatching("config.yml") {
            expand(
                    "version" to project.version
            )
        }
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")

        relocate("org.bstats", "dev.mrzcookie.zchat.libraries.bstats")
        relocate("net.kyori", "dev.mrzcookie.zchat.libraries.kyori")

        minimize()
    }
}