package com.example.playlistmaker.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistHorizontalItemBinding
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistHorizontalAdapter (
    private val listener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistHorizontalViewHolder> () {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHorizontalViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistHorizontalViewHolder(PlaylistHorizontalItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistHorizontalViewHolder, position: Int) {
        holder.bind(playlists[position], listener)
    }

    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}