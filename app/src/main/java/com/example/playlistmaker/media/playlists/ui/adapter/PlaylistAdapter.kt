package com.example.playlistmaker.media.playlists.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistAdapter (
    private val listener: PlaylistClickListener
) : ListAdapter<Playlist, PlaylistViewHolder>(AsyncDifferConfig.Builder(ItemComparator()).build()) {
    // можно просто ItemComparator(),  без AsyncDifferConfig.Builder(T).build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(currentList[position], listener)
    }

    override fun getItemCount() = currentList.size

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }

    class ItemComparator: DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.playlistId == newItem.playlistId
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }

}