package com.example.playlistmaker.search.domain.models

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