plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false


}
buildscript {

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}
