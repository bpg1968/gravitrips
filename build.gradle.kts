plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "ca.bgiroux"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

private val javafxModules = listOf("javafx.controls", "javafx.fxml")

application {
    mainClass.set("ca.bgiroux.gravitrips.view.ConnectFourApp")
    applicationDefaultJvmArgs = listOf(
        "--module-path", "\$APP_HOME/lib",
        "--add-modules", javafxModules.joinToString(",")
    )
}

javafx {
    version = "21.0.2"
    modules = javafxModules
}

tasks.test {
    useJUnitPlatform()
}
