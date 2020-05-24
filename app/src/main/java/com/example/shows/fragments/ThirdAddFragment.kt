package com.example.shows.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shows.R
import kotlinx.android.synthetic.main.fragment_add_third.*

class ThirdAddFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genre : String = arguments?.getString("genre").toString()
        add_genre.text = genre
    }

    companion object {
        fun newInstance() =
            ThirdAddFragment()
    }
}