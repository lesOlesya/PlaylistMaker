package com.example.playlistmaker.player.domain

interface PlayerInteractor {

    suspend fun createUpdateTimerTask()

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(statusObserver: PlayerRepository.StatusObserver)

    fun releasePlayer()
}