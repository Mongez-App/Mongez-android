plugins {
    id("mongez.android.library.compose")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.navigation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
