package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}