plugins {
    id("mongez.android.library")
    id("mongez.android.hilt")
    alias(libs.plugins.ksp)
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
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play)
    implementation(libs.google.id)
    implementation(libs.kotlinx.coroutines.play.services)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}


