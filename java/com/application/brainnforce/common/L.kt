package com.application.brainnforce.common

import android.util.Log
import com.application.brainnforce.BuildConfig

class L{

    companion object{

        fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        fun i(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.i(tag, message)
            }
        }

        fun v(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.v(tag, message)
            }
        }

        fun e(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message)
            }
        }
    }

}