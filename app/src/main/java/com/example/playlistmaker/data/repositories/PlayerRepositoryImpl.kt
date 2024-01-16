package com.example.playlistmaker.data.repositories

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.example.playlistmaker.domain.api.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(private val context: Context,
                           private val play: ToggleButton,
                           private val tvTrackTime: TextView,
                           private val url: String?) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val timerRunnable = Runnable { createUpdateTimerTask() }
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun createUpdateTimerTask() {
        if (playerState == STATE_PLAYING) {
            tvTrackTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            mainThreadHandler.postDelayed(timerRunnable, DELAY)
        } else mainThreadHandler.removeCallbacks(timerRunnable)
    }

    override fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.isChecked = false
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(timerRunnable)
            tvTrackTime.text = "00:00"
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        play.isChecked = true
        playerState = STATE_PLAYING
        mainThreadHandler.post(timerRunnable)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        play.isChecked = false
        playerState = STATE_PAUSED
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    override fun playbackControl() {
        when(playerState) {
            STATE_DEFAULT -> {
                play.isChecked = true
                Toast.makeText(
                    context,
                    "Отрывок трека отсутствует",
                    Toast.LENGTH_LONG
                ).show()
                play.isChecked = false
            }
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 400L
    }

}