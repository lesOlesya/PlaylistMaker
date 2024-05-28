package com.example.playlistmaker.player.domain

interface PlayerRepository {

    suspend fun createUpdateTimerTask()

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl(statusObserver: StatusObserver)

    fun releasePlayer()

    interface StatusObserver {
        fun onProgress(progress: String)
        fun onStop()
        fun onPlay()
    }
}