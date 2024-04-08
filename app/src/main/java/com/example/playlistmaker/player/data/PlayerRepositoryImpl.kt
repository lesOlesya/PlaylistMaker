package com.example.playlistmaker.player.data

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerStates
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val simpleDateFormat: SimpleDateFormat,
    private val context: Context
) : PlayerRepository {

    private val timerRunnable = Runnable { createUpdateTimerTask() }
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private lateinit var statusObserver: PlayerRepository.StatusObserver

    private var playerState = PlayerStates.STATE_DEFAULT

    override fun createUpdateTimerTask() {
        if (playerState == PlayerStates.STATE_PLAYING) {
            statusObserver.onProgress(
                simpleDateFormat.format(mediaPlayer.currentPosition)
            )
            mainThreadHandler.postDelayed(timerRunnable, DELAY)
        } else mainThreadHandler.removeCallbacks(timerRunnable)
    }

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStates.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            statusObserver.onStop()
            playerState = PlayerStates.STATE_PREPARED
            mainThreadHandler.removeCallbacks(timerRunnable)
            statusObserver.onProgress("00:00")
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        statusObserver.onPlay()
        playerState = PlayerStates.STATE_PLAYING
        mainThreadHandler.post(timerRunnable)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        statusObserver.onStop()
        playerState = PlayerStates.STATE_PAUSED
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    override fun playbackControl(statusObserver: PlayerRepository.StatusObserver) {
        this.statusObserver = statusObserver
        when (playerState) {
            PlayerStates.STATE_DEFAULT -> {
                statusObserver.onPlay()
                Toast.makeText(
                    context,
                    "Отрывок трека не загружен",
                    Toast.LENGTH_LONG
                ).show()
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
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    companion object {
        private const val DELAY = 400L
    }

}