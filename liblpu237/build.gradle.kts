plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "kr.co.elpusk.android.liblpu237"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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

group = "kr.co.elpusk.android.liblpu237"
version = "1.5.0"

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

tasks.register<Javadoc>("generateJavadoc") {
    // 소스 파일 지정
    source = files(
            "src/main/java/kr/co/elpusk/android/liblpu237/ApiInterface.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/ApiFactory.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/UsbDevHandle.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/lpu237/Lpu237DoneCallback.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/lpu237/Lpu237GetSetCallback.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/lpu237/Lpu237Callback.java",
            "src/main/java/kr/co/elpusk/android/liblpu237/lpu237/Lpu237Tags.java"
    ).asFileTree

    // classpath 설정
    val androidJar = fileTree("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/") {
        include("android.jar")
    }
    val coreLambdaStubs = fileTree("${android.sdkDirectory}/build-tools/${android.buildToolsVersion}/") {
        include("core-lambda-stubs.jar")
    }
    classpath = files(
            android.bootClasspath + androidJar + coreLambdaStubs
    )+files(android.sourceSets["main"].java.srcDirs+"kr/co/elpusk/android/liblpu237")

    // Javadoc 옵션 설정
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        charSet = "UTF-8"
        docEncoding = "UTF-8"
        locale = "ko_KR"
        memberLevel = JavadocMemberLevel.PROTECTED
        addStringOption("Xdoclint:none", "-quiet")
        links("https://developer.android.com/reference/")
    }

}