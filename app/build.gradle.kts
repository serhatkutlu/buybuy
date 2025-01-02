plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
    id ("androidx.navigation.safeargs")
    id("kotlin-parcelize")


}

android {
    namespace = "com.example.buybuy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.buybuy"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"https://fakestoreapi.in/api/\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }
    buildFeatures{
        buildConfig=true
    }
}

dependencies {






    implementation(project(":core:network"))
    implementation(project(":core:workmanager"))
    implementation(project(":core:common"))


    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.navigation.fragment.ktx.v240)
    implementation (libs.androidx.navigation.ui.ktx.v240)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.runtime.android)
    implementation(libs.androidx.hilt.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.drawerlayout)


    //circleimageview
    implementation (libs.circleimageview)
    // Apply Hilt Gradle plugin
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.logging.interceptor)

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    // coroutine
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)


    implementation (libs.shimmer)

    implementation (libs.lottie)

    implementation (libs.androidx.swiperefreshlayout)

    implementation (libs.firebase.messaging)

    debugImplementation (libs.squareup.leakcanary.android)
    implementation (libs.guava)

}