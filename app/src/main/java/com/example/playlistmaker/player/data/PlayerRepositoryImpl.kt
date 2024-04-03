package com.example.playlistmaker.player.data

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerStates
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private val context: Context,
    private val url: String?,
    private val statusObserver: StatusObserver
) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerStates.STATE_DEFAULT
    private val timerRunnable = Runnable { createUpdateTimerTask() }
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun createUpdateTimerTask() {
        if (playerState == PlayerStates.STATE_PLAYING) {
            statusObserver.onProgress(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
            )
            mainThreadHandler.postDelayed(timerRunnable, DELAY)
        } else mainThreadHandler.removeCallbacks(timerRunnable)
    }

    override fun preparePlayer() {
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

    override fun playbackControl() {
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

    interface StatusObserver {
        fun onProgress(progress: String)
        fun onStop()
        fun onPlay()
    }

    companion object {
        private const val DELAY = 400L
    }

}