package com.iti.mongez

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("mongez.android.library")
            plugins.apply("mongez.compose")
        }
    }
}
