package com.b305.vuddy.model

import android.app.Application
import com.b305.vuddy.util.SharedManager

//class App : Application() {
//
//    companion object {
//        lateinit var instance: App
//        lateinit var context: Context
//    }
//
//    override fun onCreate() {
//        super.onCreate()

//        instance = this
//        context = applicationContext
//    }
//}
class App : Application() {
    companion object {
        lateinit var instance: SharedManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = SharedManager(applicationContext)
    }
}
