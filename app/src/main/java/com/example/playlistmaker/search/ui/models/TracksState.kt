package com.example.playlistmaker.search.ui.models

import com.example.playlistmaker.search.domain.models.Track

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