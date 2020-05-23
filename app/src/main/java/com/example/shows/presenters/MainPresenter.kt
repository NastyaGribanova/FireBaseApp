package com.example.shows.presenters

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shows.repository.SignInRepository
import com.example.shows.recycler.Shows
import com.example.shows.recycler.ShowsAdapter
import com.example.shows.views.MainView
import com.google.android.gms.ads.AdView
import moxy.MvpPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val repository: SignInRepository
): MvpPresenter<MainView>() {

    private var list: ArrayList<Shows>? = null
    private lateinit var adapter: ShowsAdapter

    fun authUser(){
        if (!repository.authUser()){
            viewState.goToAuth()
        }
    }

    fun initAd(adView: AdView){
        repository.initAdd(adView)
    }

    fun openDialog(){
        viewState.openDialog()
    }
    fun changesFromDialog(name:String, genre:String, index: Int){
        val listSize = list?.size
        var newIndex = index
        if (index > listSize ?:0) {
            newIndex = listSize ?:0
        }
        list?.add(newIndex, Shows(name, genre))
        list?.let { adapter.updateList(it) }
    }

    fun closeDialog(){
        viewState.closeDialog()
    }

    fun signOut(){
        repository.signOut()
    }

    fun delete(shows: Shows) {
        list?.remove(shows)
        list?.let { adapter.updateList(it) }
    }

    fun initRecycler(recyclerView: RecyclerView){
        adapter = ShowsAdapter(getDataSource()) { shows ->
            delete(shows)
        }
        recyclerView.adapter = adapter
        setRecyclerViewItemTouchListener(recyclerView)
    }

    fun setRecyclerViewItemTouchListener(recyclerView: RecyclerView) {
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
                list?.removeAt(index)
                list?.let { adapter.updateList(it) }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        recyclerView.addItemDecoration(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun destroyAd(){
        repository.destroyAd()
    }

    fun causeCrash() {
        Log.w("Crashlytics", "Crash button clicked");
        throw NullPointerException("Fake null pointer exception")
    }

    fun getDataSource(): ArrayList<Shows> = arrayListOf(
        Shows("Supernatural", "supernatural"),
        Shows("How I met your mother", "comedy"),
        Shows("Friends", "comedy"),
        Shows("Death note", "anime"),
        Shows("Black Mirror", "fantasy"),
        Shows("Devs", "sci-fi")
    )
}
