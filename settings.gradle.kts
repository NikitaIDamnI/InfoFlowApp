include(":lib")

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
include(":core:opennews_api")
include(":core:database")
include(":core:data")
include(":core:common")
include(":features:news_main")
include(":features:search")
include(":features:navigation")
include(":features:detailed_news")
include(":features:uikit")
include(":domain")
include(":baselineprofile")

