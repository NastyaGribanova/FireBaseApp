package com.example.shows.repository

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.fabric.sdk.android.Fabric
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SignInRepositoryImpl @Inject constructor(val context: Context,
                                               val firebaseAnalytics: FirebaseAnalytics,
                                               val auth: FirebaseAuth,
                                               var gso: GoogleSignInOptions): SignInRepository,
    GoogleApiClient.OnConnectionFailedListener {
    private var googleApiClient: GoogleApiClient? = null
    private var verificationInProgress = false
    private var adView: AdView? = null

    override fun createAccount(email: String, password: String):Boolean {
        var result = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                } else {
                    Log.d("SignUp", "createUserWithEmail:success")
                    result = true
                }
            }
        return result
    }

    override fun signIn(email: String, password: String):Boolean {
        var result = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("SignIn", "signInUserWithEmail:failure")
                } else {
                    Log.d("SignIn", "signInUserWithEmail:success")
                    result = true
                }
            }
        return result
    }

    override fun signInWithGoogle(string: String):Boolean{
        googleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(context as FragmentActivity, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        return true
    }

    override fun signOut() {
        auth.signOut()
        //Auth.GoogleSignInApi.signOut(googleApiClient)
    }

    override fun reserPassword(email: String):Boolean {
        var result = false
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { listener ->
                if (listener.isSuccessful) {
                    Log.d("Firebase", "We have sent you instructions to reset your password!")
                    result = true
                } else {
                    Log.d("Firebase", "We haven't sent you instructions to reset your password!")
                }
            }
        return result
    }

    override fun authUser(): Boolean {
        return (auth.currentUser !=  null)
    }

    override fun initAdd(adView: AdView) {
        Fabric.with(context, Crashlytics())
        val adRequest =
            AdRequest.Builder().build()
        adView.loadAd(adRequest)
        analitics(adRequest)
    }

    override fun analitics(adRequest: AdRequest) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, adRequest.toString())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun destroyAd() {
        if (adView != null) {
            adView?.destroy()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("Connection", "onConnectionFailed:$connectionResult")
    }
}
