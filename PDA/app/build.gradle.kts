plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}
apply("plugin.gradle")
android {
    namespace = "com.cainiao.chuanliu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cainiao.chuanliu.pda"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "V1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // 设置APK文件名
            applicationVariants.all {
                outputs.all {output->
                    val outputFileName = "川流PDA-${buildType.name}-v${versionName}.apk"
                    (output as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = outputFileName
                    return@all true
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    lint {
        abortOnError = false
    }
    buildFeatures {
        buildConfig = true
    }
    dataBinding { enable = true }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.10.0")
    implementation(group = "com.squareup.okhttp3", name = "logging-interceptor", version = "4.10.0")
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.11.0")
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.11.0")
    implementation(group = "com.squareup.retrofit2", name = "converter-gson", version = "2.11.0")
    implementation(group = "com.squareup.retrofit2", name = "adapter-rxjava3", version = "2.11.0")
    implementation(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.1.8")
    implementation(group = "io.reactivex.rxjava3", name = "rxandroid", version = "3.0.1")
    implementation(group = "io.reactivex.rxjava3", name = "rxkotlin", version = "3.0.1")
    implementation(group = "com.github.getActivity", name = "ToastUtils", version = "10.5")

    val room_version = "2.5.0"
    kapt(libs.room.compiler)
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.8.0")
    implementation(group = "androidx.room", name = "room-runtime", version = room_version)
    implementation(group = "androidx.room", name = "room-ktx", version = room_version)
    implementation(group = "androidx.room", name = "room-rxjava2", version = room_version)
    implementation(group = "androidx.room", name = "room-guava", version = room_version)
    implementation(group = "androidx.room", name = "room-paging", version = room_version)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("com.guolindev.permissionx:permissionx:1.8.0")
    implementation(group = "org.apache.poi", name = "poi", version = "3.9")
    implementation(group = "org.apache.poi", name = "poi-excelant", version = "3.9")
    implementation(group = "org.apache.poi", name = "poi-scratchpad", version = "3.9")
    implementation(files("libs/com.cainiao.myexcel.1.0.aar"))
    implementation(files("libs/com.cainiao.mycommon.1.0.aar"))
    implementation(files("libs/com.cainiao.mywidget.1.0.aar"))
}
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}