# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\android_studio\android-sdk-windows/tools/proguard/proguard-android.txt
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
#-keepattributes EnclosingMethod
#
#-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
#-optimizationpasses 5            # 指定代码的压缩级别
#-dontusemixedcaseclassnames      # 是否使用大小写混合
#-dontpreverify                   # 混淆时是否做预校验
#-verbose                         # 混淆时是否记录日志
#-keepattributes *Annotation*     # 保持注解
#-ignorewarning                   # 忽略警告
#-dontoptimize                    # 优化不优化输入的类文件

#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#保持哪些类不被混淆
-keep public class * extends com.ubtechinc.alpha.sdk.**
-keep public class * extends com.ubtechinc.alpha.serverlibutil.interfaces.**

#生成日志数据，gradle build时在本项目根目录输出
#-dump class_files.txt            #apk包内所有class的内部结构
#-printseeds seeds.txt            #未混淆的类和成员
#-printusage unused.txt           #打印未被使用的代码
#-printmapping mapping.txt        #混淆前后的映射

#-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}