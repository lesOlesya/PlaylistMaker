package com.example.playlistmaker.player.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId = intent.getIntExtra("TrackId", 0)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(trackId)
        )[PlayerViewModel::class.java]

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }

        viewModel.getTrackLiveData().observe(this) { track ->
            binding.trackNameTvAudioPlayer.text = track.trackName
            binding.artistNameTvAudioPlayer.text = track.artistName
            binding.durationTvAudioPlayer.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)!!
            binding.albumTvAudioPlayer.text = track.collectionName.orEmpty()
            binding.yearTvAudioPlayer.text = track.releaseDate?.substring(0, 4).orEmpty()
            binding.genreTvAudioPlayer.text = track.primaryGenreName.orEmpty()
            binding.countryTvAudioPlayer.text = track.country.orEmpty()
            Glide.with(this)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8F, binding.artworkIvAudioPlayer.context)))
                .into(binding.artworkIvAudioPlayer)
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            binding.playButton.isChecked = playStatus.isPlaying
            binding.trackTimeTvAudioPlayer.text = playStatus.progress
        }

        binding.playButton.setOnClickListener {
            viewModel.play()
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}

