package io.rm.log4j2.android.sample

import android.app.Application
import io.rm.log4j2.android.Log4j2Android

class Log4j2AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Log4j2Android.init(applicationContext = this.applicationContext, configXmlResource = R.raw.log4j_android_debug)
        } else {
            Log4j2Android.init(applicationContext = this.applicationContext, configXmlResource = R.raw.log4j_android_release)
        }
    }
}