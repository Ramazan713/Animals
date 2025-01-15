import org.jetbrains.kotlin.cli.jvm.main
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.google.services)
    alias(libs.plugins.firebase.crashlytics.gradle)
    alias(libs.plugins.firebase.perf.plugin)
    alias(libs.plugins.room)
    alias(libs.plugins.android.junit5)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.masterplus.animals"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.masterplus.animals"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations += arrayOf("tr","en")

        buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["INTERSTITIAL_TEST_AD_ID"]}\"")
        buildConfigField("String","REWARDED_AD_ID","\"${keystoreProperties["REWARDED_TEST_AD_ID"]}\"")
        buildConfigField("String","APP_CHECK_DEBUG_TOKEN","\"${keystoreProperties["APP_CHECK_DEBUG_TOKEN"]}\"")
        buildConfigField("String","ALGOLIA_APP_ID","\"${keystoreProperties["ALGOLIA_DEV_APP_ID"]}\"")
        buildConfigField("String","ALGOLIA_API_KEY","\"${keystoreProperties["ALGOLIA_DEV_API_KEY"]}\"")
        manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        manifestPlaceholders["ADMOB_APP_ID"] = "${keystoreProperties["ADMOB_APP_ID"]}"
    }

    signingConfigs {

        create("release"){
            keyAlias = "${keystoreProperties["RELEASE_KEY_ALIAS"]}"
            keyPassword = "${keystoreProperties["RELEASE_KEY_PASSWORD"]}"
            storeFile = file(keystoreProperties["RELEASE_STORE_FILE"]!!)
            storePassword = "${keystoreProperties["RELEASE_STORE_PASSWORD"]}"
        }

        create("staging"){
            keyAlias = "${keystoreProperties["STAGING_KEY_ALIAS"]}"
            keyPassword = "${keystoreProperties["STAGING_KEY_PASSWORD"]}"
            storeFile = file(keystoreProperties["STAGING_STORE_FILE"]!!)
            storePassword = "${keystoreProperties["STAGING_STORE_PASSWORD"]}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["AUTH_PROD_CLIENT_ID"]}\"")
            buildConfigField("String","INTERSTITIAL_AD_ID","\"${keystoreProperties["INTERSTITIAL_AD_ID"]}\"")
            buildConfigField("String","REWARDED_AD_ID","\"${keystoreProperties["REWARDED_AD_ID"]}\"")
            buildConfigField("String","ALGOLIA_APP_ID","\"${keystoreProperties["ALGOLIA_PROD_APP_ID"]}\"")
            buildConfigField("String","ALGOLIA_API_KEY","\"${keystoreProperties["ALGOLIA_PROD_API_KEY"]}\"")
        }
        debug {
            sourceSets {
                getByName("debug"){
                    assets.srcDir("src/debug/assets")
                }
            }
            applicationIdSuffix = ".debug"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["AUTH_DEV_CLIENT_ID"]}\"")
        }

        create("staging"){
            sourceSets {
                getByName("staging"){
                    assets.srcDir("src/staging/assets")
                }
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("staging")
            applicationIdSuffix = ".staging"
            buildConfigField("String","AUTH_CLIENT_ID","\"${keystoreProperties["AUTH_DEV_CLIENT_ID"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.mockk.android)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit)
    testImplementation(libs.assertk)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    implementation(libs.bundles.lifecycle)

    implementation(libs.navigation.compose)
    implementation(libs.navigation.ui.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.lib.zoomable)
    implementation(libs.compose.shimmer)
    implementation(libs.admob.ads)
    implementation(libs.algoliasearch)
    implementation(libs.ktor.client)

    implementation(libs.bundles.paging)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.credentials)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.coil)
}