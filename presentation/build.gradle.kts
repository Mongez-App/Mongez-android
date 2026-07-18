plugins {
    id("mongez.android.library.compose")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.presentation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":design_system"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

