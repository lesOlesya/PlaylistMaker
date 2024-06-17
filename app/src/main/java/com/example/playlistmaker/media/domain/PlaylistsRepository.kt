package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist, track: Track) : Boolean

    fun getPlaylists(): Flow<List<Playlist>>

}