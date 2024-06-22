package com.example.playlistmaker.media.playlists.domain.impl

import com.example.playlistmaker.media.playlists.domain.PlaylistsInteractor
import com.example.playlistmaker.media.playlists.domain.PlaylistsRepository
import com.example.playlistmaker.media.playlists.domain.model.Playlist
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

    override suspend fun deletePlaylist(playlist: Playlist) {
        return playlistsRepository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        val playlist = GlobalScope.async { playlistsRepository.getPlaylistById(playlistId) }
        return playlist.await()
    }

    override fun getPlaylistsTracks(trackIds: ArrayList<Int>): Flow<List<Track>> {
        return playlistsRepository.getPlaylistsTracks(trackIds)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        return playlistsRepository.deleteTrackFromPlaylist(playlist, track)
    }
}