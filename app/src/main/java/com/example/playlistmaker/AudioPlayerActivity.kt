package com.example.playlistmaker

import android.R.attr.button
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val timerRunnable = Runnable { createUpdateTimerTask() }

    private lateinit var play: ToggleButton
    private lateinit var tvTrackTime: TextView
    private var url: String? = null
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackId = intent.getIntExtra("TrackId", 0)
        val track = getTrack(trackId)

        mainThreadHandler = Handler(Looper.getMainLooper())
        play = findViewById(R.id.play_button)
        tvTrackTime = findViewById(R.id.track_time_tv_audio_player)
        val backButton = findViewById<ImageButton>(R.id.audio_player_back)
        val ivArtwork =  findViewById<ImageView>(R.id.artwork_iv_audio_player)
        val tvTrackName = findViewById<TextView>(R.id.track_name_tv_audio_player)
        val tvArtistName = findViewById<TextView>(R.id.artist_name_tv_audio_player)
        val tvDuration = findViewById<TextView>(R.id.duration_tv_audio_player)
        val tvAlbum = findViewById<TextView>(R.id.album_tv_audio_player)
        val tvYear = findViewById<TextView>(R.id.year_tv_audio_player)
        val tvGenre = findViewById<TextView>(R.id.genre_tv_audio_player)
        val tvCountry = findViewById<TextView>(R.id.country_tv_audio_player)

        backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(track!!.getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8F, ivArtwork.context)))
            .into(ivArtwork)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvDuration.text = track.getTrackTime()
        tvAlbum.text = track.collectionName ?: " "
        tvYear.text = track.releaseDate?.substring(0, 4) ?: " "
        tvGenre.text = track.primaryGenreName ?: " "
        tvCountry.text = track.country ?: " "
        url = track.previewUrl

        if (url != null) preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private fun createUpdateTimerTask() {
        if (playerState == STATE_PLAYING) {
            tvTrackTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(timerRunnable, DELAY)
        } else mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.isChecked = false
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(timerRunnable)
            tvTrackTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.isChecked = true
        playerState = STATE_PLAYING
        mainThreadHandler?.post(timerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.isChecked = false
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_DEFAULT -> {
                play.isChecked = true
                Toast.makeText(
                    this,
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

    private fun getTrack(trackId: Int) : Track? {
        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val tracks: ArrayList<Track> = Gson().fromJson(json, type)

        for (i in 0 until tracks.size) {
            if (trackId == tracks[i].trackId) return tracks[i]
        }
        return null
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 400L
    }
}

