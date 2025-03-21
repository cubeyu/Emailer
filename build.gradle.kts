plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.lagbug"
version = "2.1.8"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.16.5")
    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("javax.activation:activation:1.1.1")
    implementation("com.github.technicallycoded:FoliaLib:0.4.3")
}

val targetJavaVersion = 8
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}
tasks {
    shadowJar {
        dependencies {
            include(dependency("com.sun.mail:javax.mail:1.6.2"))
            include(dependency("javax.activation:activation:1.1.1"))
            include(dependency("com.github.technicallycoded:FoliaLib:0.4.3"))
        }
        mapOf(
            "com.tcoded.folialib" to "folialib"
        ).forEach { (original, target) ->
            relocate(original, "me.lagbug.emailer.spigot.libs.$target")
        }
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
    withType<JavaCompile> {
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand("version" to version)
            include("plugin.yml")
        }
    }
}
