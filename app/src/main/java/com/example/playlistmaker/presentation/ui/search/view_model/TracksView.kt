package com.example.playlistmaker.presentation.ui.search.view_model

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.search.models.TracksState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TracksView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: TracksState)

}