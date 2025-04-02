plugins {
    id("application")
}

application {
    mainClass.set("org.example.server.MainServer")
}

dependencies {
    implementation(project(":common"))
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "org.example.server.MainServer")
    }
}
