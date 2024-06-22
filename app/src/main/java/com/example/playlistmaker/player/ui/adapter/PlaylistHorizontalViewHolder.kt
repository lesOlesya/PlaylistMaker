package com.example.playlistmaker.player.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistHorizontalItemBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.util.dpToPx

class PlaylistHorizontalViewHolder(private val binding: PlaylistHorizontalItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val ivCover = binding.playlistSmallCoverIv

    fun bind(playlist: Playlist, listener: PlaylistHorizontalAdapter.PlaylistClickListener) {

        Glide.with(itemView)
            .load(playlist.coverUri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(dpToPx(2F, ivCover.context)))
            .into(ivCover)

        binding.playlistName.text = playlist.playlistName
        binding.tracksCount.text = itemView.resources.getQuantityString(R.plurals.trackCount,
            playlist.tracksCount, playlist.tracksCount)

        itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }
    }
}