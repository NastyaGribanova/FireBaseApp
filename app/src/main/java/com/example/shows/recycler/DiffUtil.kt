package com.example.shows.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffUtil(private val oldLList: List<Shows>, private val newList: List<Shows>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldLList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldLList[oldItemPosition] == newList[newItemPosition]

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldLList[oldItemPosition] == newList[newItemPosition]
}
