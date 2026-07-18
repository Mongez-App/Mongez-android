plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
    id("mongez.android.network")
}

android {
    namespace = "com.iti.mongez.data"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.datastore.preferences)
}
