# AndroidX Core KTX
-keep class androidx.core.** { *; }

# AndroidX Lifecycle Runtime KTX
-keep class androidx.lifecycle.** { *; }

# AndroidX Activity Compose
-keep class androidx.activity.compose.** { *; }

# AndroidX Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# AndroidX UI
-keep class androidx.ui.** { *; }
-dontwarn androidx.ui.**

# AndroidX Material3
-keep class com.google.android.material.** { *; }

# JUnit & AndroidX Test
-dontwarn org.junit.**
-dontwarn androidx.test.**
-dontwarn androidx.test.ext.junit.runners.**
-dontwarn org.hamcrest.**
-dontwarn kotlinx.coroutines.test.**

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**
-keep,allowobfuscation @interface kotlinx.serialization.Serializable
-keepclassmembers,allowobfuscation class * {
    @kotlinx.serialization.Serializable <fields>;
}

# AndroidX Navigation Compose
-keep class androidx.navigation.** { *; }

# AndroidX Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keepclassmembers class * {
    @androidx.room.Dao *;
}
-keepclassmembers class * {
    @androidx.room.Query *;
}

# Dagger Hilt
-dontwarn dagger.hilt.android.**
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# Retrofit, OkHttp, Gson
-keep class com.google.gson.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.Interceptor
-dontwarn okio.**

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# AndroidX Extended Icons
-keep class com.google.android.material.internal.** { *; }

# AndroidX Multidex
-keep class androidx.multidex.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Vertex AI
-keep class com.google.firebase.ml.modeldownloader.** { *; }
-keep class org.tensorflow.lite.** { *; }