plugins {
    application
    java
}

group = "ca.bgiroux"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val javafxVersion = "21.0.2"
val javafxModules = listOf("javafx.controls", "javafx.fxml")

// Determine platform classifier for JavaFX runtime artifacts.
val os = System.getProperty("os.name").lowercase()
val arch = System.getProperty("os.arch").lowercase()
val platform = when {
    os.contains("win") && (arch.contains("aarch64") || arch.contains("arm64")) -> "win-aarch64"
    os.contains("win") -> "win"
    os.contains("mac") && (arch.contains("aarch64") || arch.contains("arm64")) -> "mac-aarch64"
    os.contains("mac") -> "mac"
    os.contains("linux") && (arch.contains("aarch64") || arch.contains("arm64")) -> "linux-aarch64"
    else -> "linux"
}

dependencies {
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform") { isTransitive = false }
    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform") { isTransitive = false }
    implementation("org.openjfx:javafx-fxml:$javafxVersion:$platform") { isTransitive = false }
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform") { isTransitive = false }

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("ca.bgiroux.gravitrips.view.ConnectFourApp")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<CreateStartScripts>().configureEach {
    val jarTask = tasks.named<Jar>("jar")
    classpath = files(jarTask.map { it.archiveFile.get().asFile })
    doLast {
        val mainJar = jarTask.get().archiveFileName.get()
        val unixScriptContent = """
            |#!/bin/sh
            |APP_HOME="$(cd "$(dirname "$0")/.." && pwd)"
            |CACHE_DIR="${'$'}APP_HOME/cache"
            |mkdir -p "${'$'}CACHE_DIR"
            |JAVA_EXEC="${'$'}{JAVA_HOME:+${'$'}JAVA_HOME/bin/java}"
            |JAVA_EXEC="${'$'}{JAVA_EXEC:-java}"
            |exec "${'$'}JAVA_EXEC" -Djavafx.cachedir="${'$'}CACHE_DIR" --module-path "${'$'}APP_HOME/lib" --add-modules ${javafxModules.joinToString(",")} -cp "${'$'}APP_HOME/lib/$mainJar" ${application.mainClass.get()} "${'$'}@"
        """.trimMargin()
        unixScript.writeText(unixScriptContent)
        unixScript.setExecutable(true)

        val windowsScriptContent = """
            |@echo off
            |set APP_HOME=%~dp0..
            |set CACHE_DIR=%APP_HOME%\cache
            |if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"
            |set JAVA_EXEC=%JAVA_HOME%\bin\java.exe
            |if "%JAVA_HOME%"=="" set JAVA_EXEC=java
            |"%JAVA_EXEC%" -Djavafx.cachedir="%CACHE_DIR%" --module-path "%APP_HOME%\lib" --add-modules ${javafxModules.joinToString(",")} -cp "%APP_HOME%\lib\\$mainJar" ${application.mainClass.get()} %*
        """.trimMargin()
        windowsScript.writeText(windowsScriptContent)
    }
}

tasks.named<JavaExec>("run") {
    jvmArgs(
        "--module-path", configurations.runtimeClasspath.get().asPath,
        "--add-modules", javafxModules.joinToString(",")
    )
}
