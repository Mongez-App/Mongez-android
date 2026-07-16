plugins {
    id("mongez.android.library")
}

android {
    namespace = "com.iti.mongez.data"
}

dependencies {
    implementation(project(":domain"))
}
