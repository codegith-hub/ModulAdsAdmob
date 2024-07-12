plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.adsmedia.adsmodul"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    implementation (libs.play.services.ads)
    implementation (libs.facebook)
    implementation (libs.applovin)
    implementation (libs.unity)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.user.messaging.platform)
    implementation(libs.pangle)
    implementation (libs.masterads)
}
android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.codegith-hub"
                artifactId = "ModulAdsAdmob"
                version = "1.0.0"
                from(components["release"])
            }
        }
    }
}
