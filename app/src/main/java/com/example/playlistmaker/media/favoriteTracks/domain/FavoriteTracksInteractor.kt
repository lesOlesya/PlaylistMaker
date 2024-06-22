package com.example.playlistmaker.media.favoriteTracks.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}