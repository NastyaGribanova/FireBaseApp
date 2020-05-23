package com.example.shows.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shows.R
import kotlinx.android.synthetic.main.fragment_add_first.*

class FirstAddFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name : String = arguments?.getString("name").toString()
        add_name.text = name
    }

    companion object {
        fun newInstance() =
            FirstAddFragment()
    }
}