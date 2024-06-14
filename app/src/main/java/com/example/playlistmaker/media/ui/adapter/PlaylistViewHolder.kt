package com.example.playlistmaker.media.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media.domain.model.Playlist
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
        binding.tracksCount.text = convertor(playlist.tracksCount)

        itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }
    }

    private fun convertor(num: Int) : String {
        val ost10 = num % 10
        val ost100 = num % 100
        return if (ost10 == 1 && ost100 != 11) ("$num трек")
        else if (ost10 in intArrayOf(2, 3, 4) && ost100 !in intArrayOf(12, 13, 14)) ("$num трека")
        else ("$num треков")
    }
}