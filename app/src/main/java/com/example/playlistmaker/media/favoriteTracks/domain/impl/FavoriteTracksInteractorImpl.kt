package com.example.playlistmaker.media.favoriteTracks.domain.impl

import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun addTrack(track: Track) {
        return favoriteTracksRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        return favoriteTracksRepository.deleteTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }
}