
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation("aws.sdk.kotlin:dynamodb:1.4.96")
    implementation("aws.sdk.kotlin:s3:1.4.96")

    implementation("aws.smithy.kotlin:http-client-engine-ktor-jvm:0.7.8")

//    implementation("org.slf4j:slf4j-simple:1.7.30")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")
//    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
//    implementation("org.apache.logging.log4j:log4j-api:2.17.2")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7") // 👈 This line fixes the CIO issue


}
