package com.example.playlistmaker.player.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val simpleDateFormat: SimpleDateFormat by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId = intent.getIntExtra("TrackId", 0)

        val viewModel: PlayerViewModel by viewModel {
            parametersOf(trackId)
        }

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }

        viewModel.getTrackLiveData().observe(this) { track ->
            binding.trackNameTvAudioPlayer.text = track.trackName
            binding.artistNameTvAudioPlayer.text = track.artistName
            binding.durationTvAudioPlayer.text =
                simpleDateFormat.format(track.trackTimeMillis)!!
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

