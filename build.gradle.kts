plugins {
    application
    java
}

tasks.named<JavaExec>("run"){
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.openai:openai-java:4.6.1")
}

application {
    mainClass.set("app.ResearchAgent")
//    mainClass.set("TellAJoke")
}
