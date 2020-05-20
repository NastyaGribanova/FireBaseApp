package com.example.shows.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.ListAdapter

class ShowsAdapter(
    private var dataSource: ArrayList<Shows>,
    private val clickLambda: (Shows) -> Unit
) : ListAdapter<Shows, ShowsHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsHolder =
        ShowsHolder.create(parent, clickLambda)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ShowsHolder, position: Int) =
        holder.bind(dataSource[position])

    fun updateList(newList: ArrayList<Shows>) {
        calculateDiff(
            DiffUtil(this.dataSource, newList),
            true
        )
            .dispatchUpdatesTo(this)
        this.dataSource.clear()
        this.dataSource.addAll(newList)
    }
}
