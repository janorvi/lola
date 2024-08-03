package com.example.lolaecu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Lola : Application() {

    companion object{
//        lateinit var selinaPrefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
//        selinaPrefs = Prefs(applicationContext)
    }
}