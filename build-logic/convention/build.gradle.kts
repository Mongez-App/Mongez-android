plugins {
    `kotlin-dsl`
}

group = "com.iti.mongez.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.hilt.android.plugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "mongez.android.library"
            implementationClass = "com.iti.mongez.AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "mongez.android.application"
            implementationClass = "com.iti.mongez.AndroidApplicationConventionPlugin"
        }
        register("compose") {
            id = "mongez.compose"
            implementationClass = "com.iti.mongez.ComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mongez.android.library.compose"
            implementationClass = "com.iti.mongez.AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "mongez.kotlin.library"
            implementationClass = "com.iti.mongez.KotlinLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "mongez.android.hilt"
            implementationClass = "com.iti.mongez.AndroidHiltConventionPlugin"
        }
    }
}
