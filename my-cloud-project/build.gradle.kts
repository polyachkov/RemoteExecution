plugins {
    id("java")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

subprojects {
    apply(plugin = "java")
    
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
