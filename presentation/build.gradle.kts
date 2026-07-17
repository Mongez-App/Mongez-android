plugins {
    id("mongez.android.library.compose")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.iti.mongez.presentation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":design_system"))

    implementation(libs.androidx.navigation.compose)
    
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
