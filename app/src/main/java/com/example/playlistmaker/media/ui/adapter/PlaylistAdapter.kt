package com.example.playlistmaker.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistAdapter (
    private val listener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistViewHolder> () {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], listener)
    }

    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}