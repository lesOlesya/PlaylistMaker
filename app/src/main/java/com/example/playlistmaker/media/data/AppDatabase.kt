package com.example.playlistmaker.media.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.playlists.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.favoriteTracks.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.media.playlists.data.db.dao.PlaylistsTrackDao
import com.example.playlistmaker.media.favoriteTracks.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistsTrackEntity

@Database(version = 3, entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistsTrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistsTrackDao(): PlaylistsTrackDao
}