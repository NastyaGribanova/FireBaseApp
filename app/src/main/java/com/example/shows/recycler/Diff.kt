package com.example.shows.recycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

object Diff : DiffUtil.ItemCallback<Shows>() {

    override fun areItemsTheSame(oldItem: Shows, newItem: Shows): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Shows, newItem: Shows): Boolean =
        oldItem.genre == newItem.genre

    override fun getChangePayload(oldItem: Shows, newItem: Shows): Any? {
        val diffBundle = Bundle()
        if (oldItem.name != newItem.name) {
            diffBundle.putString("name", newItem.name)
        }
        if (oldItem.genre != newItem.genre) {
            diffBundle.putString("genre", newItem.genre)
        }
        return if (diffBundle.isEmpty) null else diffBundle
    }

}
