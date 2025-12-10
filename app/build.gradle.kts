plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "estore.istic.fr"
    compileSdk = 35

    defaultConfig {
        applicationId = "estore.istic.fr"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = false
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.legacy.support.core.utils)
    implementation (platform(libs.firebase.bom))
    implementation (libs.play.services.auth)

    implementation (libs.firebase.auth.v2107)
    implementation (libs.firebase.ui.auth)
    implementation (libs.firebase.database.v2005)
    implementation (libs.firebase.ui.database)
    implementation (libs.firebase.firestore.v2422)
    implementation (libs.firebase.storage.v2001)

    // splash
    implementation (libs.core.splashscreen)

    // notifications
    implementation(libs.firebase.messaging)

    // implementation(libs.pinview)
    implementation(libs.pinview)

    // alert dialog
    implementation(libs.material.v161)

    // imageSlider
    implementation(libs.imageslideshow)

    //bottom navigation bar
    implementation(libs.library)
    implementation(libs.superbottombar)
    implementation(libs.chip.navigation.bar)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:")

    // Picasso
    implementation(libs.picasso)

    // google
    implementation (libs.firebase.appcheck.safetynet.v1601)
    implementation (libs.browser.v140)

    // shimmer effect when loading elements
    implementation(libs.shimmer)

    // lottie
    implementation(libs.lottie)

    // ticket view
    implementation(libs.zigzagview)

}