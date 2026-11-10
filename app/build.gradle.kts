import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safeargs)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

val animalsApiBaseUrl = localProperties.getProperty(
    "ANIMALS_API_BASE_URL",
    "http://10.0.2.2:8080/"
)

android {
    namespace = "com.velykoivanstanislav.animals"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.velykoivanstanislav.animals"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "ANIMALS_API_BASE_URL", "\"$animalsApiBaseUrl\"")
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val mockitoAgent = configurations.create("mockitoAgent")

abstract class MockitoAgentArgumentProvider : CommandLineArgumentProvider {
    @get:Classpath
    abstract val agentClasspath: ConfigurableFileCollection

    override fun asArguments() = listOf("-javaagent:${agentClasspath.singleFile.absolutePath}")
}

tasks.withType<Test>().configureEach {
    jvmArgumentProviders.add(objects.newInstance<MockitoAgentArgumentProvider>().apply {
        agentClasspath.from(mockitoAgent)
    })
}

dependencies {
    mockitoAgent(libs.mockito.core) {
        isTransitive = false
    }
    // Java / XML UI
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)

    // ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // Fragment navigation and Java Safe Args
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    // Images and colors
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.palette)

    // Dependency injection
    implementation(libs.dagger.core)
    implementation(libs.dagger.android.support)
    annotationProcessor(libs.dagger.compiler)
    annotationProcessor(libs.dagger.android.processor)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.arch.core.testing)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
