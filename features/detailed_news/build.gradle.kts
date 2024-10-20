plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.detail_news"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.coil.compose)

    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.foundation.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.material)
    implementation(libs.material.icons)

    implementation(libs.jsoup)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)



    api(libs.kotlinx.immutable)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.gson)


    //UI
    compileOnly(libs.androidx.compose.runtime)




    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":navigation"))
    implementation(project(":uikit"))


}