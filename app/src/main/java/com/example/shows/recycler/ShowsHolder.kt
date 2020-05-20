package com.example.shows.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_show.*

class ShowsHolder (
    override val containerView: View,
    private val clickLambda: (Shows) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(shows: Shows) {
        tv_name.text = shows.name
        tv_genre.text = shows.genre

        itemView.setOnClickListener {
            clickLambda(shows)
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickLambda: (Shows) -> Unit) =
            ShowsHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false),
                clickLambda
            )
    }
}
