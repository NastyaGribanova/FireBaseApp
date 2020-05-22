package com.example.shows

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.shows.presenters.MainPresenter
import com.example.shows.views.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


class MainActivity : MvpAppCompatActivity(), MainView {
    lateinit var dialog: AlertDialog

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>

    private val presenter: MainPresenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
        initRecycler()
        initAdd()
        presenter.authUser()
    }

    private fun initListeners() {
        btn_show_dialog.setOnClickListener{
            presenter.openDialog()
        }
    }

    override fun onDestroy() {
        presenter.destroyAd()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.crash -> {
                Log.w("Crashlytics", "Crash button clicked")
                presenter.causeCrash()
                true
            }
            R.id.logout -> {
                presenter.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun openDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog, null)
        val builder = this.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        dialog = builder.show()
        dialogView.btn_add_dialog.setOnClickListener {
            presenter.closeDialog()
            val name = dialogView.et_name_dialog.text.toString()
            val genre = dialogView.et_genre.text.toString()
            val index = dialogView.et_index_dialog.text.toString().toInt() - 1

            presenter.changesFromDialog(name,genre,index)
        }
        dialogView.btn_cancel_dialog.setOnClickListener {
            presenter.closeDialog()
        }
    }

    override fun closeDialog(){
        dialog.dismiss()
    }

    override fun initRecycler() {
        presenter.initRecycler(rv_shows)
    }

    override fun goToAuth() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    override fun initAdd() {
        presenter.initAd(findViewById(R.id.adView))
    }
}
