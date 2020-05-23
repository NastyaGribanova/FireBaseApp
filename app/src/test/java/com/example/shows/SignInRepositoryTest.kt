package com.example.shows

import android.content.Context
import com.example.shows.repository.SignInRepositoryImpl
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SignInRepositoryTest {

    var repository: SignInRepositoryImpl? = null

    @Before
    fun beforeFun() {
        val context: Context = Mockito.mock(Context::class.java)
        val firebaseAnalytics: FirebaseAnalytics = Mockito.mock(FirebaseAnalytics::class.java)
        val auth: FirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
        val gso: GoogleSignInOptions = Mockito.mock(GoogleSignInOptions::class.java)
        repository = SignInRepositoryImpl(context, firebaseAnalytics, auth, gso)
    }

    @Test
    fun signIn() {
        Assert.assertEquals(repository?.signIn("email", "password"), false)
    }
}
