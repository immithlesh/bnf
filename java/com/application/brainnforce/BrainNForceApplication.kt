package com.application.brainnforce

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BrainNForceApplication : Application() {

    companion object {
        var appContext: Application? = null
         const val TAG = "GoogleActivity"
         const val RC_SIGN_IN = 9001

    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}