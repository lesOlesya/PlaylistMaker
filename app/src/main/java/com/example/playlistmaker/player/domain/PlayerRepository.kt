package com.example.playlistmaker.player.domain

interface PlayerRepository {

    fun createUpdateTimerTask()

    fun preparePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun playbackControl()

    fun releasePlayer()
}