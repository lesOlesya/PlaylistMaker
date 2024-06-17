package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.media.data.db.entity.PlaylistsTrackEntity

@Dao
interface PlaylistsTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistsTrackEntity)
}