plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
}

android {
    namespace = "com.iti.mongez.data"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.datastore.preferences)

    // Network dependencies
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.google.gson)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
}
