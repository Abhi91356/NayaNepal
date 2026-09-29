pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Naya Nepal"
include(":app")

// Core Modules
include(":core:network")
include(":core:designsystem")
include(":core:auth")
include(":core:ml")

// Feature Modules
include(":feature:wallet")
include(":feature:marketplace")
include(":feature:gig")
include(":feature:learning")
include(":feature:agri")
include(":feature:health")
include(":feature:tourism")
include(":feature:gov")
include(":feature:profile")
include(":feature:notifications")
include(":feature:accounting")
