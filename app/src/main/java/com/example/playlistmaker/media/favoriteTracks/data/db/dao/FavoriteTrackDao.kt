package com.example.playlistmaker.media.favoriteTracks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.favoriteTracks.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete()
    suspend fun deleteTrack(favoriteTrackEntity: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table ORDER BY time_of_add DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT id FROM favorite_track_table")
    suspend fun getTracksId(): List<Int>
}
