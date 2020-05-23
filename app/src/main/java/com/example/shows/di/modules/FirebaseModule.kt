package com.example.shows.di.modules

import android.content.Context
import com.example.shows.R
import com.example.shows.di.scopes.ApplicationScope
import com.example.shows.repository.SignInRepository
import com.example.shows.repository.SignInRepositoryImpl
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @ApplicationScope
    fun provideRep(repository: SignInRepositoryImpl): SignInRepository = repository

    @Provides
    @ApplicationScope
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @ApplicationScope
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    @ApplicationScope
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString())
            .requestEmail()
            .build()
}
