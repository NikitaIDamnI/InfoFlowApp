
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

rootProject.name = "InfoFlow"
include(":app")
include(":opennews_api")
include(":database")
include(":data")
include(":features:news_main")
include(":common")
include(":features:search")
include(":navigation")
include(":features:detailed_news")
include(":domain")
include(":uikit")
include(":baselineprofile")

