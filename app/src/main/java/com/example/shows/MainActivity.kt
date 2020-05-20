package com.example.shows

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.example.shows.recycler.Shows
import com.example.shows.recycler.ShowsAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*

class MainActivity : AppCompatActivity() {
    private val list: ArrayList<Shows> = getDataSource()
    private var adapter: ShowsAdapter? = null
    private var adView: AdView? = null
    private var username: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null
    private var googleApiClient: GoogleApiClient? = null
    private var firebaseAnalytics: FirebaseAnalytics? = null
    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null
    private var messageEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth?.getCurrentUser()
        username = "ANONYMOUS"

        if (firebaseUser == null) {
            startActivity( Intent(this, SignInActivity::class.java))
            finish()
        } else {
            username = firebaseUser?.getDisplayName();
            username = firebaseUser?.getEmail();
        }

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, GoogleApiClient.OnConnectionFailedListener { })
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build()

        initAd()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        adapter = ShowsAdapter(getDataSource()) { shows ->
            delete(shows)
        }
        rv_shows.adapter = adapter
        setRecyclerViewItemTouchListener()
        btn_show_dialog.setOnClickListener {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            initRemoteConfig()
            showDialog()
        }
    }

    private fun initRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val firebaseRemoteConfigSettings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .setDeveloperModeEnabled(true)
                .build()
        val defaultConfigMap: MutableMap<String, Any> = HashMap()
        defaultConfigMap["friendly_msg_length"] = 10L
        firebaseRemoteConfig?.setConfigSettings(firebaseRemoteConfigSettings)
        firebaseRemoteConfig?.setDefaults(defaultConfigMap)
        fetchConfig()
    }

    private fun fetchConfig(){
        var cacheExpiration = 3600
        if (firebaseRemoteConfig?.getInfo()?.getConfigSettings()?.isDeveloperModeEnabled()!!) {
            cacheExpiration = 0
        }
        firebaseRemoteConfig?.fetch(cacheExpiration.toLong())
            ?.addOnSuccessListener {
                firebaseRemoteConfig?.activateFetched();
                applyRetrievedLengthLimit();
            }
            ?.addOnFailureListener {
                Log.w("Log", "Error fetching config");
                applyRetrievedLengthLimit();
            };
    }

    private fun applyRetrievedLengthLimit(){
        var friendly_msg_length = firebaseRemoteConfig?.getLong("friendly_msg_length")
        if (friendly_msg_length != null) {

            messageEditText?.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(friendly_msg_length.toInt())))
        }
        Log.d("Log", "FML is:$friendly_msg_length")
    }

    private fun initAd() {
        adView = findViewById(R.id.adView)
        val adRequest =
            AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                viewHolder1: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val index = viewHolder.adapterPosition
                list.removeAt(index)
                adapter?.updateList(list)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        rv_shows.addItemDecoration(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(rv_shows)
    }

    private fun delete(shows: Shows) {
        list.remove(shows)
        adapter?.updateList(list)
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog, null)
        val builder = this.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val dialog = builder?.show()
        dialogView.btn_add_dialog.setOnClickListener {
            dialog?.dismiss()
            val name = dialogView.et_name_dialog.text.toString()
            val genre = dialogView.et_genre.text.toString()
            var index = dialogView.et_index_dialog.text.toString().toInt() - 1
            val listSize = list.size
            if (index > listSize) {
                index = listSize
            }
            list.add(index, Shows(name, genre))
            adapter?.updateList(list)
        }
        dialogView.btn_cancel_dialog.setOnClickListener {
            dialog?.dismiss()
        }

    }

    private fun getDataSource(): ArrayList<Shows> = arrayListOf(
        Shows("Supernatural", "supernatural"),
        Shows("How I met your mother", "comedy"),
        Shows("Friends", "comedy"),
        Shows("Death note", "anime"),
        Shows("Black Mirror", "fantasy"),
        Shows("Devs", "sci-fi")
    )

    override fun onDestroy() {
        if (adView != null) {
            adView?.destroy()
        }
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.crash -> {
                Log.w("Crashlytics", "Crash button clicked")
                causeCrash()
                true
            }
            R.id.logout -> {
                firebaseAuth!!.signOut()
                Auth.GoogleSignInApi.signOut(googleApiClient)
                firebaseUser = null
                username = "ANONYMOUS"
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun causeCrash() {
        throw NullPointerException("Fake null pointer exception")
    }
}
