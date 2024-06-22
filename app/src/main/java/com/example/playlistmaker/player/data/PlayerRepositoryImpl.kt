package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val simpleDateFormat: SimpleDateFormat
) : PlayerRepository {

    private var timerJob: Job? = null

    private lateinit var statusObserver: PlayerRepository.StatusObserver

    private var playerState = PlayerStates.STATE_DEFAULT

    override suspend fun createUpdateTimerTask() {
        while(playerState == PlayerStates.STATE_PLAYING) {
            if (mediaPlayer.currentPosition < 29400) {
                statusObserver.onProgress(
                    simpleDateFormat.format(mediaPlayer.currentPosition)
                )
            }
            delay(DELAY)
        }
    }

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStates.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            statusObserver.onStop()
            playerState = PlayerStates.STATE_PREPARED
            statusObserver.onProgress("00:00")
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        statusObserver.onPlay()
        playerState = PlayerStates.STATE_PLAYING
        timerJob = GlobalScope.launch(Dispatchers.Main) {
            createUpdateTimerTask()
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        statusObserver.onStop()
        playerState = PlayerStates.STATE_PAUSED
        timerJob?.cancel()
    }

    override fun playbackControl(statusObserver: PlayerRepository.StatusObserver) {
        this.statusObserver = statusObserver
        when (playerState) {
            PlayerStates.STATE_DEFAULT -> {
                statusObserver.onPlay()
                statusObserver.showToast()
                statusObserver.onStop()
            }

            PlayerStates.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        timerJob?.cancel()
    }

    companion object {
        private const val DELAY = 300L
    }
}