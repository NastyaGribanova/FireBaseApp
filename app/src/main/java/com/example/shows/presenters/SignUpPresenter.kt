package com.example.shows.presenters

import com.example.shows.repository.SignInRepository
import com.example.shows.views.SignUpView
import moxy.MvpPresenter
import javax.inject.Inject

class SignUpPresenter @Inject constructor(
    private val repository: SignInRepository
): MvpPresenter<SignUpView>() {

    fun signUp(email: String, password: String) {
        if(repository.createAccount(email,password)) viewState.goToMain()
    }

    fun signUpWithGoogle(string: String) {
        if(repository.signInWithGoogle(string)) viewState.goToMain()
    }

    fun goToAuth(){
        viewState.goToAuth()
    }

    fun authUser(){
        if (repository.authUser()){
            viewState.goToMain()
        }
    }
}
