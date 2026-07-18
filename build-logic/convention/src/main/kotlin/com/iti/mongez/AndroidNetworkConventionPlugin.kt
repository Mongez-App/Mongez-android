package com.iti.mongez

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidNetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // Provides core Retrofit capabilities & GSON serialization models
                add("implementation", libs.findLibrary("retrofit-core").get())
                add("implementation", libs.findLibrary("retrofit-converter-gson").get())
                add("implementation", libs.findLibrary("google-gson").get())
                add("implementation", libs.findLibrary("firebase-auth").get())
                add("implementation", libs.findLibrary("firebase-messaging").get())
            }
        }
    }
}