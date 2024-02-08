package com.example.playlistmaker.player.domain

class GetTrackCoverUseCase(private val repository: TrackCoverRepository) {

    fun execute() {
        return repository.getTrackCover()
    }

}