package com.example.shows

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.shows.fragments.FirstFragment
import moxy.MvpAppCompatActivity

class MazeActivity: MvpAppCompatActivity() {
    private val navigator = object : SupportAppNavigator(this, R.layout.main_container) {

        override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
            Screens.AUTH_SCREEN -> Intent(this@MazeActivity, SignInActivity::class.java)
            else -> null
        }

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.FIRST_SCREEN -> FirstFragment.newInstance()
            else -> null
        }
    }
}
