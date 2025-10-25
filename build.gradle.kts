plugins {
    application     // lets us run with `gradlew run`
    java
}

repositories {
    mavenCentral()  // where dependencies come from
}

dependencies {
    implementation("com.openai:openai-java:4.6.1")           // OpenAI Java SDK
}

application {
    mainClass.set("TellAJoke") // your main class (see below)
}
