package com.example.playlistmaker.playlist.ui.state

import com.example.playlistmaker.search.domain.models.Track
interface PlaylistsTracksState {

    data class Content(
        val tracks: List<Track>
    ) : PlaylistsTracksState

    data class Empty(
        val code: Int
    ) : PlaylistsTracksState

}