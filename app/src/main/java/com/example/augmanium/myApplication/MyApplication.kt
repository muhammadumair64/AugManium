package com.example.augmanium.myApplication

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {
    companion object{
        val context = this
    }
    override fun onCreate() {
        super.onCreate()

    }
}