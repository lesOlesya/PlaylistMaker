package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class GetTrackByIdUseCase(private val repository: TracksRepository) {

    fun execute(trackId: Int): Track? {

        val tracks = repository.getTracks()
        for (i in 0 until tracks.size) {
            if (trackId == tracks[i].trackId) return tracks[i]
        }
        return null
    }
}