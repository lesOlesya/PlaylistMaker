package com.example.playlistmaker.media.playlists.data.convertor

import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistsTrackEntity
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {

    private val type = object : TypeToken<ArrayList<Int>>() {}.type

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.timeOfUpdate,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from String to Uri
            gson.fromJson<ArrayList<Int>>(playlist.trackIdsList, type), // from String to ArrayList<Int>
            playlist.tracksCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.timeOfUpdate,
            playlist.playlistName,
            playlist.playlistDescription ?: "",
            playlist.coverUri, /// need from Uri to String
            gson.toJson(playlist.trackIdsList), // from ArrayList<Int> to String
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

    fun map(track: PlaylistsTrackEntity): Track {
        return Track(
            track.id,
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

    fun map(trackIdsList: String): ArrayList<Int> {
        return gson.fromJson<ArrayList<Int>>(trackIdsList, type)
    }
}