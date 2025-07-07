package com.yodiet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YoDiet : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}