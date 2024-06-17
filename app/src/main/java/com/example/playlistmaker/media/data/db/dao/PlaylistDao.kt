package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

//    @Query("UPDATE playlist_table SET tracksList=:trackList WHERE tracksCount = :tracksCount")
//    suspend fun updatePlaylist(trackList: String, tracksCount: Int)

    @Query("SELECT * FROM playlist_table ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

}