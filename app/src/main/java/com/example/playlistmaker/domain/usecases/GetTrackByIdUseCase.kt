package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class GetTrackByIdUseCase(private val repository: SearchHistoryRepository) {

    fun execute(trackId: Int): Track? {

        val tracks = repository.getHistory()
        for (i in tracks.indices) {
            if (trackId == tracks[i].trackId) return tracks[i]
        }
        return null
    }
}