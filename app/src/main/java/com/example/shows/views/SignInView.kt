package com.example.shows.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface SignInView: MvpView {
    fun goToRegister()
    fun goToMain()
}
