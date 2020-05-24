package com.example.shows.di

import com.example.shows.MazeActivity
import com.example.shows.di.modules.MazeModule
import com.example.shows.di.scopes.MazeScope
import dagger.Subcomponent

@MazeScope
@Subcomponent(modules = [MazeModule::class])
interface MazeComponent {
    fun inject(mazeActivity: MazeActivity)
}
