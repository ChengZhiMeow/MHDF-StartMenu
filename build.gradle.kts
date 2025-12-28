// 插件
plugins {
    id("java") // Java
    kotlin("jvm") version "2.1.21" // Kotlin
    id("com.gradleup.shadow") version "8.3.3" // Shadow
    id("xyz.jpenilla.run-paper") version "2.3.1" // Run Paper
}

// 项目信息
group = "cn.chengzhimeow"
version = "1.0.0"
kotlin.jvmToolchain(21)
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots")
    maven("https://repo.codemc.io/repository/maven-releases")
    maven("https://repo.papermc.io/repository/maven-public")
    maven("https://oss.sonatype.org/content/groups/public")
    maven("https://repo.fancyinnovations.com/releases")
    maven("https://repo-eo.catnies.top/releases")
    maven("https://repo-eo.catnies.top/mhdf")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21")
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    compileOnly("cn.chengzhimeow:CC-Scheduler:2.0.4")
    implementation("cn.chengzhimeow:CC-Condition:2.0.20")
    implementation("cn.chengzhimeow:CC-Action:1.0.21")
    implementation("cn.chengzhiya:MHDF-Item:1.0.0")
    compileOnly("cn.chengzhimeow:CC-Yaml:2.1.22")

    compileOnly("com.github.retrooper:packetevents-spigot:2.11.0")
    compileOnly("de.oliver:FancyHolograms:2.9.0")
}

// 任务配置
tasks {
    clean {
        delete("$rootDir/target")
    }

    processResources {
        expand("version" to project.version)
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        exclude("META-INF/")

        archiveFileName.set("[梦之开始菜单]${project.name}-${project.version}.jar")
        destinationDirectory.set(file("$rootDir/target"))
//        destinationDirectory.set(file("C:\\Users\\ChengZhiYa\\Desktop\\momi\\plugins"))
    }

    // 调试测试
    runServer {
        dependsOn(shadowJar)
        dependsOn(jar)

        minecraftVersion("1.21.8")
        downloadPlugins {
            hangar("PlaceholderAPI", "2.11.6")
        }
    }
}
