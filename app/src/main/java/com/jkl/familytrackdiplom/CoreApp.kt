package com.jkl.familytrack

import android.app.Application
import android.content.Context
import com.jkl.familytrack.di.appModule
import org.koin.android.ext.android.startKoin

class CoreApp: Application() {

    companion object{
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        configureDI()
    }

    private fun configureDI() {
        startKoin(this, appModule)
    }


}