package com.example.playlistmaker.player.domain

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override suspend fun createUpdateTimerTask() {
        repository.createUpdateTimerTask()
    }

    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl(statusObserver: PlayerRepository.StatusObserver) {
        repository.playbackControl(statusObserver)
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }
}