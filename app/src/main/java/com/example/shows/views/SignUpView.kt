package com.example.shows.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface SignUpView: MvpView {
    fun goToAuth()
    fun goToMain()
}
