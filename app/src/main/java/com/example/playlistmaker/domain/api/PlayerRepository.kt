package com.example.playlistmaker.domain.api

interface PlayerRepository {

    fun createUpdateTimerTask()

    fun preparePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()
}