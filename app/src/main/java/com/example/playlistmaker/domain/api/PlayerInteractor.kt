package com.example.playlistmaker.domain.api

interface PlayerInteractor {

    fun createUpdateTimerTask()

    fun preparePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()
}