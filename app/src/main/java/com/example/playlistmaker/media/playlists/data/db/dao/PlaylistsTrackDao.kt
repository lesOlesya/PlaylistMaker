package com.example.playlistmaker.media.playlists.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.playlists.data.db.entity.PlaylistsTrackEntity

@Dao
interface PlaylistsTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistsTrackEntity)

    @Query("DELETE FROM playlists_track_table WHERE id = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM playlists_track_table")
    suspend fun getAllTracks(): List<PlaylistsTrackEntity>
}