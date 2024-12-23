plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.example.infoflow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.infoflow"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


            val keyFromProperties = project.findProperty("NEWS_API_KEY")?.toString()

            val apiKey = if (keyFromProperties == null || keyFromProperties == "null") {
                System.getenv("NEWS_API_KEY") ?: error(
                    "NEWS_API_KEY not found in environment variables or gradle.properties!"
                )
            } else {
                keyFromProperties
            }


        buildConfigField("String", "NEWS_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "NEWS_API_BASE_URL", "\"https://newsapi.org/v2/\"")

        resourceConfigurations += setOf("ru", "en")
        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }

    }

    signingConfigs {
        create("release") {
            storeFile = File(rootDir, "newsapp.keystore")
            keyPassword =
                System.getenv("RELEASE_KEY_PASSWORD") ?: project.findProperty("RELEASE_KEY_PASSWORD").toString()
            keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: project.findProperty("RELEASE_KEY_ALIAS").toString()
            storePassword =
                System.getenv("RELEASE_STORE_PASSWORD") ?: project.findProperty("RELEASE_STORE_PASSWORD").toString()

        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }


        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = "17"
        }

        buildFeatures {
            buildConfig = true
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                excludes += "/okhttp3/internal/publicsuffix/NOTICE"
                excludes += "/kotlin/**"
                excludes += "META-INF/androidx.*.version"
                excludes += "META-INF/com.google.*.version"
                excludes += "META-INF/kotlinx_*.version"
                excludes += "kotlin-tooling-metadata.json"
                excludes += "DebugProbesKt.bin"
                excludes += "META-INF/com/android/build/gradle/*"
            }
        }

    }

    dependencies {
        implementation(libs.dagger.hilt.android)
        implementation(libs.androidx.profileinstaller)
        kapt(libs.dagger.hilt.compiler)
        //Initial dependencies
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        debugImplementation(libs.okhttp.logging.interceptor)

        implementation(project(":core:data"))
        implementation(project(":core:opennews_api"))
        implementation(project(":core:database"))
        implementation(project(":core:common"))
        implementation(project(":features:news_main"))
        implementation(project(":features:search"))
        implementation(project(":features:detailed_news"))
        implementation(project(":features:navigation"))
        implementation(project(":features:uikit"))
        baselineProfile(project(":baselineprofile"))
    }
}

