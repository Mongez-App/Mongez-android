plugins {
    id("mongez.android.library.compose")
}

android {
    namespace = "com.iti.mongez.presentation"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":design_system"))

    implementation(libs.androidx.navigation.compose)
}
