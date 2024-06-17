package com.example.playlistmaker.media.ui.state

import com.example.playlistmaker.media.domain.model.Playlist

interface PlaylistsState {

//    object Loading : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data class Empty(
        val code: Int
    ) : PlaylistsState

}