package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.PlaylistsInteractor
import com.example.playlistmaker.media.domain.PlaylistsRepository
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        return playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) : Boolean {
        val isSuccessful = GlobalScope.async { playlistsRepository.updatePlaylist(playlist, track) }
        return isSuccessful.await()
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }
}