package com.example.shows.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface MainView: MvpView {
    fun openDialog()
    fun initRecycler()
    fun goToAuth()
    fun initAdd()
    fun closeDialog()
}
