package com.example.shows

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.shows.fragments.FirstFragment
import com.example.shows.fragments.SecondFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.main_container.*
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class MazeActivity: MvpAppCompatActivity() {

    private val navigator : Navigator = SupportAppNavigator(this, R.id.nav_host_fragment)
    lateinit var navController: NavController
    val bundle = Bundle()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.provideMazeComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_container)
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.first -> {
                        navController.navigate(R.id.firstFragment)
                        true
                    }
                    R.id.second -> {
                        navController.navigate(R.id.secondFragment)
                        true
                    }
                    R.id.third -> {
                        navController.navigate(R.id.thirdFragment)
                        true
                    }
                    else -> false
                }
            }
        btv_main.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    fun onButton(view: View){
        bundle.putString("name", name.text.toString())
        Navigation.findNavController(view)
            .navigate(R.id.action_firstFragment_to_firstAddFragment, bundle)
    }

    fun onSecondButton(view: View){
        Navigation.findNavController(view)
            .navigate(R.id.action_secondFragment_to_mainActivity)
    }

    fun onThirdButton(view: View){
        bundle.putString("genre", genre.text.toString())
        Navigation.findNavController(view)
            .navigate(R.id.action_thirdFragment_to_thirdAddFragment, bundle)
    }
}
