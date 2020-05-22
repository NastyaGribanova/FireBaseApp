package com.example.shows.di

import android.content.Context
import com.example.shows.SignInActivity
import com.example.shows.MainActivity
import com.example.shows.SignUpActivity
import com.example.shows.di.modules.AppModule
import com.example.shows.di.modules.FirebaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, FirebaseModule::class])
interface AppComponent {

    fun getContext(): Context
    fun inject(activity: SignInActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}
