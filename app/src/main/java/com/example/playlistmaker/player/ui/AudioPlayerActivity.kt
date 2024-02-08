package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator.providePlayerInteractor
import com.example.playlistmaker.creator.Creator.provideTrackByIdUseCase
import com.example.playlistmaker.creator.Creator.provideTrackCoverUseCase
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    private val getTrackByIdUseCase by lazy { provideTrackByIdUseCase(applicationContext) }

    private lateinit var play: ToggleButton
    private lateinit var tvTrackTime: TextView
    private var url: String? = null

    private val playerInteractorImpl by lazy { providePlayerInteractor(applicationContext, play, tvTrackTime, url) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

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
        binding.durationTvAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)!!
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

