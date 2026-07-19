plugins {
    id("mongez.kotlin.library")
}

dependencies {
    implementation(libs.inject)
    implementation(libs.kotlinx.coroutines.core)
}
