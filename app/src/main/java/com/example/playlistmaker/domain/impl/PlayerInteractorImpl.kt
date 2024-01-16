package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun createUpdateTimerTask() {
        repository.createUpdateTimerTask()
    }

    override fun preparePlayer() {
        repository.preparePlayer()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }
}