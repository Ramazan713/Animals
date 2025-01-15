# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.masterplus.animals.core.data.dtos.** {*;}

-keep enum com.masterplus.animals.core.domain.enums.ContentType { *;}
-keep enum com.masterplus.animals.core.domain.enums.CategoryType { *;}
-keep enum com.masterplus.animals.core.domain.enums.KingdomType { *;}
-keep enum com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType { *;}



