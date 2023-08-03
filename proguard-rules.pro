# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
-dontshrink
-dontoptimize

##material
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.* { *; }

##recyclerview
-keep public class * extends androidx.recyclerview.widget.RecyclerView$LayoutManager {
    public <init>(...);
}



##Butterknife
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

##Google analytics
 -keep class com.google.android.gms.** { *; }
 -keep public class com.google.android.gms.**
 -dontwarn com.google.android.gms.*

##Facebook
-keep class com.facebook.** { *; }
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

##one signal
-dontwarn com.onesignal.**
# These 2 methods are called with reflection
-keep class com.google.android.gms.common.api.GoogleApiClient {
    void connect();
    void disconnect();
}
-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}
# Observer backcall methods are called with reflection

-dontwarn com.amazon.**
# Proguard ends up removing this class even if it is used in AndroidManifest.xml so force keeping it.

##firebase
-dontwarn com.google.firebase.**
-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}
-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

##anjlab
-keep class com.android.vending.billing.**