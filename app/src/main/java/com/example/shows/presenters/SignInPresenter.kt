package com.example.shows.presenters

import com.example.shows.repository.SignInRepository
import com.example.shows.views.SignInView
import moxy.MvpPresenter
import javax.inject.Inject

class SignInPresenter @Inject constructor(
    private val repository: SignInRepository
): MvpPresenter<SignInView>() {

    fun signIn(email: String, password: String) {
        if(repository.signIn(email,password)) viewState.goToMain()
    }

    fun signInWithGoogle(string: String) {
        if(repository.signInWithGoogle(string)) viewState.goToMain()
    }

    fun goToRegister() {
        viewState.goToRegister()
    }

    fun resetPassword(email: String) {
        repository.reserPassword(email)
    }

    fun authUser(){
        if (repository.authUser()){
            viewState.goToMain()
        }
    }
}
