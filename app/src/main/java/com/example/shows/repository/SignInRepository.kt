package com.example.shows.repository

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

interface SignInRepository {
    fun createAccount(email: String, password: String):Boolean
    fun signIn(email: String, password: String):Boolean
    fun signInWithGoogle(string: String):Boolean
    fun signOut()
    fun reserPassword(email: String):Boolean
    fun authUser():Boolean
    fun initAdd(adView: AdView)
    fun analitics(adRequest: AdRequest)
    fun destroyAd()
}
