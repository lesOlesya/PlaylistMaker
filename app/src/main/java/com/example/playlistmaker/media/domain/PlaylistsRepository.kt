package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}