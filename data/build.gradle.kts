plugins {
    id("mongez.android.library")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.iti.mongez.data"
}

dependencies {
    implementation(project(":domain"))
    
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    
    implementation(libs.androidx.datastore.preferences)
}
