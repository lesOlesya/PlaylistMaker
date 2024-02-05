package com.example.playlistmaker.presentation.ui.search.models

import com.example.playlistmaker.domain.models.Track

interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        val errorCode: Int
    ) : TracksState

    data class Empty(
        val code: Int
    ) : TracksState

}