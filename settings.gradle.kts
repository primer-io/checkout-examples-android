pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        maven {
           setUrl(providers.gradleProperty("PRIMER_ANDROID_ARTIFACTORY_URL"))
        }
        mavenCentral()
    }
}

rootProject.name = "Checkout Examples"
include(":co-badged-cards")
include(":drop-in-checkout")
