package com.example.shows.cicerone

import android.os.Bundle
import androidx.fragment.app.Fragment


class FragmentParams {
    val fragmentClass: Class<out Fragment?>
    val arguments: Bundle?

    constructor(
        fragmentClass: Class<out Fragment?>,
        arguments: Bundle?
    ) {
        this.fragmentClass = fragmentClass
        this.arguments = arguments
    }

    constructor(fragmentClass: Class<out Fragment?>) {
        this.fragmentClass = fragmentClass
        arguments = null
    }
}
