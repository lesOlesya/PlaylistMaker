package com.example.playlistmaker.player.domain

interface PlayerInteractor {

    fun createUpdateTimerTask()

    fun preparePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()
}