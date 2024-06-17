package com.example.playlistmaker.media.ui.state

import com.example.playlistmaker.search.domain.models.Track

interface FavoriteTracksState {

//    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState

    data class Empty(
        val code: Int
    ) : FavoriteTracksState

}