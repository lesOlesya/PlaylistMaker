package com.example.playlistmaker.creating_and_updating_playlist.ui.state

interface UpdatingPlaylistState {
    data class Content(
        val playlistCoverUri: String?,
        val playlistName: String,
        val playlistDescription: String?
    ) : UpdatingPlaylistState
}