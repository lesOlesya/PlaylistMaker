package com.example.playlistmaker.media.favoriteTracks.data

import com.example.playlistmaker.media.data.AppDatabase
import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksRepository
import com.example.playlistmaker.media.favoriteTracks.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.favoriteTracks.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        appDatabase.favoriteTrackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.favoriteTrackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoriteTrackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}