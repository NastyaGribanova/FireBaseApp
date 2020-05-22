package com.example.shows

import android.content.Intent
import android.os.Bundle
import com.example.shows.presenters.SignInPresenter
import com.example.shows.views.SignInView
import kotlinx.android.synthetic.main.activity_sign_in.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


class SignInActivity : MvpAppCompatActivity(), SignInView {
    @Inject
    lateinit var presenterProvider: Provider<SignInPresenter>

    private val presenter: SignInPresenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initListeners()
        presenter.authUser()
    }

    private fun initListeners() {
        val email = email.text.toString()
        val password = password.text.toString()
        btn_login.setOnClickListener { presenter.signIn(email, password) }
        sign_in_button.setOnClickListener { presenter.signInWithGoogle(getString(R.string.default_web_client_id))}
        btn_reset_password.setOnClickListener{ presenter.resetPassword(email)}
        btn_to_signup.setOnClickListener{ presenter.goToRegister()}
    }

    override fun goToRegister() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    override fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
