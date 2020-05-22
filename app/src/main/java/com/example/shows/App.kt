package com.example.shows

import android.app.Application
import com.example.shows.di.AppComponent
import com.example.shows.di.DaggerAppComponent
import com.example.shows.di.modules.AppModule
import moxy.MvpFacade

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MvpFacade.init()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
