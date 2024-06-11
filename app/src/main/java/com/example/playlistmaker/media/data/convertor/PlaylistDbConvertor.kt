package com.example.playlistmaker.media.data.convertor

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {

    private val type = object : TypeToken<ArrayList<Track>>() {}.type

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from String to Uri
            gson.fromJson<ArrayList<Track>>(playlist.tracksList, type), // from String to ArrayList<Track>
            playlist.tracksCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from Uri to String
            gson.toJson(playlist.tracksList), // from ArrayList<Track> to String
            playlist.tracksCount
        )
    }
}