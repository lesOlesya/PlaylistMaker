package com.example.playlistmaker.media.favoriteTracks.data.convertor

import com.example.playlistmaker.media.favoriteTracks.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {

//    fun map(track: TrackDto): TrackEntity {
//        return TrackEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
//    }

    fun map(track: FavoriteTrackEntity): Track {
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
            track.previewUrl,
            true
        )
    }

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            System.currentTimeMillis(),
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