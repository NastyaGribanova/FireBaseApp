package com.example.shows

import android.content.Intent
import android.os.Bundle
import com.example.shows.presenters.SignUpPresenter
import com.example.shows.views.SignUpView
import kotlinx.android.synthetic.main.activity_sign_up.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class SignUpActivity : MvpAppCompatActivity(), SignUpView {
    @Inject
    lateinit var presenterProvider: Provider<SignUpPresenter>

    private val presenter: SignUpPresenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initListeners()
        presenter.authUser()
    }

    private fun initListeners() {
        val email = email.text.toString()
        val password = password.text.toString()
        btn_signup.setOnClickListener { presenter.signUp(email,password) }
        btn_to_signin.setOnClickListener{ presenter.goToAuth()}
    }

    override fun goToAuth() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    override fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
