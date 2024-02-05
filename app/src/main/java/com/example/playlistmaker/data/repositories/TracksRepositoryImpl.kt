package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(NO_INTERNET)
            }

            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName ?: "",
                        it.artistName ?: "",
                        it.trackTimeMillis,
                        it.artworkUrl100 ?: "",
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            400 -> {
                Resource.Error(SERVER_ERROR)
            }

            else -> {
                Resource.Error(-100)
            }
        }
    }

    companion object {
        private const val NOTHING_FOUND = 0
        private const val NO_INTERNET = -1
        private const val SERVER_ERROR = 400
    }
}