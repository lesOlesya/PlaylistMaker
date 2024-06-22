package com.example.playlistmaker.media.playlists.data

import com.example.playlistmaker.media.data.AppDatabase
import com.example.playlistmaker.media.playlists.domain.PlaylistsRepository
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.media.playlists.data.convertor.PlaylistDbConvertor
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) : Boolean {
        playlist.trackIdsList.add(track.trackId)
        playlist.tracksCount++

        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))

        appDatabase.playlistsTrackDao().insertTrack(playlistDbConvertor.map(track))

        return true
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        GlobalScope.launch {
            appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
            playlist.trackIdsList.forEach {
                deleteUnusedTrack(it)
            }
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
        return playlistDbConvertor.map(playlistEntity)
    }

    override fun getPlaylistsTracks(trackIds: ArrayList<Int>): Flow<List<Track>> = flow {
        val allTracks = appDatabase.playlistsTrackDao().getAllTracks()
        val neededTracks = ArrayList<Track>()
        trackIds.forEach { id ->
            allTracks.forEach { track -> if (track.id == id) neededTracks.add(playlistDbConvertor.map(track)) }
        }
        emit(neededTracks)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        playlist.trackIdsList.remove(track.trackId)
        playlist.tracksCount--

        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))

        deleteUnusedTrack(track.trackId)
    }

    private suspend fun deleteUnusedTrack(trackId: Int) {
        val playlists = appDatabase.playlistDao().getPlaylists()
        var isUnused = true

        playlists.forEach { playlist ->
            playlistDbConvertor.map(playlist.trackIdsList).forEach { id ->
                if (trackId == id) isUnused = false
            }
        }

        if (isUnused) appDatabase.playlistsTrackDao().deleteTrack(trackId)
    }

    private fun convertFromPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlist -> playlistDbConvertor.map(playlist) }
    }

}