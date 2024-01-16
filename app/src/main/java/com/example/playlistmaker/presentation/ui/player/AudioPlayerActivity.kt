package com.example.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repositories.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackCoverRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.usecases.GetTrackByIdUseCase
import com.example.playlistmaker.domain.usecases.GetTrackCoverUseCase


class AudioPlayerActivity : AppCompatActivity() {

    private val trackByIdRepository by lazy { TracksRepositoryImpl(applicationContext) }
    private val getTrackByIdUseCase by lazy { GetTrackByIdUseCase(trackByIdRepository) }

    private lateinit var play: ToggleButton
    private lateinit var tvTrackTime: TextView
    private var url: String? = null

    private val playerRepository by lazy { PlayerRepositoryImpl(applicationContext, play, tvTrackTime, url) }
    private val playerInteractorImpl by lazy { PlayerInteractorImpl(playerRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackId = intent.getIntExtra("TrackId", 0)
        val track = getTrackByIdUseCase.execute(trackId)

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

        val trackCoverRepository = TrackCoverRepositoryImpl(this, track!!, ivArtwork)
        val getTrackCoverUseCase = GetTrackCoverUseCase(trackCoverRepository)

        getTrackCoverUseCase.execute()

        backButton.setOnClickListener {
            finish()
        }

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvDuration.text = track.getTrackTime()
        tvAlbum.text = track.collectionName ?: " "
        tvYear.text = track.releaseDate?.substring(0, 4) ?: " "
        tvGenre.text = track.primaryGenreName ?: " "
        tvCountry.text = track.country ?: " "
        url = track.previewUrl

        if (url != null) playerInteractorImpl.preparePlayer()

        play.setOnClickListener {
            playerInteractorImpl.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractorImpl.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractorImpl.releasePlayer()
    }
}

