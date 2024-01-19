package com.example.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator.providePlayerInteractor
import com.example.playlistmaker.Creator.provideTrackByIdUseCase
import com.example.playlistmaker.Creator.provideTrackCoverUseCase
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val getTrackByIdUseCase by lazy { provideTrackByIdUseCase(applicationContext) }

    private lateinit var play: ToggleButton
    private lateinit var tvTrackTime: TextView
    private var url: String? = null

    private val playerInteractorImpl by lazy { providePlayerInteractor(applicationContext, play, tvTrackTime, url) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId = intent.getIntExtra("TrackId", 0)
        val track = getTrackByIdUseCase.execute(trackId)

        play = binding.playButton
        tvTrackTime = binding.trackTimeTvAudioPlayer

        val getTrackCoverUseCase = provideTrackCoverUseCase(this, track!!, binding.artworkIvAudioPlayer)

        getTrackCoverUseCase.execute()

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }

        binding.trackNameTvAudioPlayer.text = track.trackName
        binding.artistNameTvAudioPlayer.text = track.artistName
        binding.durationTvAudioPlayer.text = track.getTrackTime()
        binding.albumTvAudioPlayer.text = track.collectionName.orEmpty()
        binding.yearTvAudioPlayer.text = track.releaseDate?.substring(0, 4).orEmpty()
        binding.genreTvAudioPlayer.text = track.primaryGenreName.orEmpty()
        binding.countryTvAudioPlayer.text = track.country.orEmpty()
        url = track.previewUrl

        url?.let { playerInteractorImpl.preparePlayer() } //if (url != null) playerInteractorImpl.preparePlayer()

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

