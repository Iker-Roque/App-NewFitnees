plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.newfitnes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newfitnes"
        minSdk = 28
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation(libs.cloudinary.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.car.ui.lib)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.room.external.antlr)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Room
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)

    //REtrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //imagen


    //implementacion de google

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")

    //barra de navegacion
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // Google Maps Compose
    implementation ("com.google.maps.android:maps-compose:4.3.3")
    implementation ("com.google.android.gms:play-services-maps:19.2.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    //Nueva implementacion

    // Mostrar imágenes desde Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.6.0")

    implementation("androidx.documentfile:documentfile:1.1.0")
    implementation("androidx.core:core:1.12.0") // Ya la tienes, solo asegúrate de usar correctamente FileProvider en el manifest

    implementation("io.coil-kt:coil-compose:2.5.0")



    implementation("androidx.documentfile:documentfile:1.0.1")





    //Material3
    implementation (libs.material3)
    implementation (libs.androidx.material.icons.extended)
    //
    //imaagen
    implementation(libs.coil.compose)

}




