package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class GetTrackByIdUseCase(private val repository: SearchHistoryRepository) {

    fun execute(trackId: Int): Track? {

        val tracks = repository.getHistory()
        for (i in tracks.indices) {
            if (trackId == tracks[i].trackId) return tracks[i]
        }
        return null
    }
}