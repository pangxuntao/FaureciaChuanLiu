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
        targetSdk = 30
        versionCode = 1
        versionName = "V1.0.1"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    // --- AndroidX 核心库 ---
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")

    // --- Kotlin 标准库 ---
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")

    // --- OkHttp & Retrofit ---
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")

    // --- RxJava3 ---
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.1")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")

    // --- Room 数据库 ---
    var room_version = "2.4.3"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // --- Lifecycle / ViewModel / LiveData ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // --- PermissionX ---
    implementation("com.guolindev.permissionx:permissionx:1.8.0")

    // --- ToastUtils ---
    implementation("com.github.getActivity:ToastUtils:10.5")

    // --- Apache POI (Excel 处理) ---
    implementation("org.apache.poi:poi:3.9")
    implementation("org.apache.poi:poi-excelant:3.9")
    implementation("org.apache.poi:poi-scratchpad:3.9")

    // --- 本地 AAR 包 ---
    implementation(files("libs/com.cainiao.myexcel.1.0.aar"))
    implementation(files("libs/com.cainiao.mycommon.1.0.aar"))
    implementation(files("libs/com.cainiao.mywidget.1.0.aar"))

    // --- DataBinding ---
    implementation("androidx.databinding:databinding-runtime:7.4.2")
    implementation("androidx.databinding:databinding-adapters:7.4.2")
    implementation("androidx.databinding:databinding-ktx:7.4.2")
}
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}