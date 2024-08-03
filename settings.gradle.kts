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
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://jitpack.io")
//            url = uri("https://gitlab.com/api/v4/projects/4128550/packages/maven")
        }
        flatDir {
            dirs("libs")
        }
    }
}

rootProject.name = "lolaecu"
include(":app")
 