package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}