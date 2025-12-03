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

application {
    mainClass.set("ca.bgiroux.gravitrips.view.ConnectFourApp")
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}
