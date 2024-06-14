package com.example.playlistmaker.media.data.convertor

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistsTrackEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {

    fun map(playlist: PlaylistEntity): Playlist {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from String to Uri
            gson.fromJson<ArrayList<Int>>(playlist.tracksList, type), // from String to ArrayList<Int>
            playlist.tracksCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from Uri to String
            gson.toJson(playlist.tracksList), // from ArrayList<Int> to String
            playlist.tracksCount
        )
    }

    fun map(track: Track): PlaylistsTrackEntity {
        return PlaylistsTrackEntity(
            track.trackId,
            track.trackName ?: "",
            track.artistName ?: "",
            track.trackTimeMillis,
            track.artworkUrl100 ?: "",
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}