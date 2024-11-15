plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.onesignal.androidsdk.onesignal-gradle-plugin")
    id("com.github.ben-manes.versions")

}

android {
    namespace = "com.jdacodes.firebaseapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jdacodes.firebaseapp"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.test.ext:junit-ktx:1.1.5")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.44")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:16.0.0")
    //Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")

    //Google Play Services
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    //Additional Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    //Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    //Gson
    implementation("com.google.code.gson:gson:2.9.1")
    //Accompanist
    implementation( "com.google.accompanist:accompanist-systemuicontroller:0.26.1-alpha")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.26.1-alpha")
    implementation ("com.google.accompanist:accompanist-pager:0.26.1-alpha")
    //One Signal
    implementation ("com.onesignal:OneSignal:[4.0.0, 4.99.99]")
    //Downloadable Custom Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    //Mockito
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation ("io.mockk:mockk:1.13.8")
    //Google truth
    testImplementation("com.google.truth:truth:1.0.1")
    androidTestImplementation("com.google.truth:truth:1.0.1")
    //MockK
    val mockkVersion = "1.13.9"
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation ("io.mockk:mockk-agent:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation( "io.mockk:mockk-agent:${mockkVersion}")



}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

