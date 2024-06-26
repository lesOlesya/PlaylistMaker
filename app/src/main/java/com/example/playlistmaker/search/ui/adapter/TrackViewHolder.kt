package com.example.playlistmaker.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.dpToPx
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat

class TrackViewHolder(private val binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val ivArtwork = binding.ivArtworkSmall

    fun bind(track: Track, listener: TrackAdapter.TrackClickListener) {

        val simpleDateFormat: SimpleDateFormat = getKoin().get()

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, ivArtwork.context)))
            .into(ivArtwork)

        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = simpleDateFormat.format(track.trackTimeMillis)!!

        itemView.setOnClickListener {
            listener.onTrackClick(track)
        }

        itemView.setOnLongClickListener {
            listener.onTrackLongClick(track)
            true
        }
    }
}