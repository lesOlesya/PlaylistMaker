package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.TrackCoverRepository

class GetTrackCoverUseCase(private val repository: TrackCoverRepository) {

    fun execute() {
        return repository.getTrackCover()
    }

}