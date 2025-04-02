plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":common"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.jsoup:jsoup:1.17.2")

}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
}
