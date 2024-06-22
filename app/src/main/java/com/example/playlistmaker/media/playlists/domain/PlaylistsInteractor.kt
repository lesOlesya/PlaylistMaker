package com.example.playlistmaker.media.playlists.domain

import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist, track: Track) : Boolean

    suspend fun deletePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Int): Playlist

    fun getPlaylistsTracks(trackIds: ArrayList<Int>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)
}