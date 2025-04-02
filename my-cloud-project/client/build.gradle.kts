plugins {
    id("application")
}

application {
    mainClass.set("org.example.client.MainClient")
}

dependencies {
    implementation(project(":common"))
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "org.example.client.MainClient")
    }
}
