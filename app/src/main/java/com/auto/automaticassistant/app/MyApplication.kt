package com.auto.automaticassistant.app

import android.app.Application

class MyApplication: Application() {

    companion object{
        lateinit var instance:MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
    }
}