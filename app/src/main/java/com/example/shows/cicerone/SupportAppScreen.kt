package com.example.shows.cicerone

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * AppScreen is base class for description and creation application screen.<br></br>
 * NOTE: If you have described the creation of Intent then Activity will be started.<br></br>
 * Recommendation: Use Intents for launch external application.
 */
abstract class SupportAppScreen : Screen() {
    val fragment: Fragment?
        get() = null

    fun getActivityIntent(context: Context): Intent? {
        return null
    }

    val fragmentParams: FragmentParams?
        get() = null
}
