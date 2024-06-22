package com.example.playlistmaker.media.playlists.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.util.dpToPx

class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val ivCover = binding.playlistCoverIv

    fun bind(playlist: Playlist, listener: PlaylistAdapter.PlaylistClickListener) {

        Glide.with(itemView)
            .load(playlist.coverUri)
            .placeholder(R.drawable.placeholder_playlist)
            .transform(CenterCrop(), RoundedCorners(dpToPx(8F, ivCover.context)))
            .into(ivCover)

        binding.playlistName.text = playlist.playlistName
        binding.tracksCount.text = itemView.resources.getQuantityString(R.plurals.trackCount,
            playlist.tracksCount, playlist.tracksCount)

        itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }
    }
}