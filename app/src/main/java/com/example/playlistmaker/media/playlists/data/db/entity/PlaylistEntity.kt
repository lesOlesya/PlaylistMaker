package com.example.playlistmaker.media.playlists.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "time_of_add")
    val timeOfUpdate: Long, // время обновления для сохранения обложки
    @ColumnInfo(name = "playlist_name")
    val playlistName: String, // Название плейлиста
    @ColumnInfo(name = "playlist_description")
    val playlistDescription: String?, // Описание плейлиста
    @ColumnInfo(name = "cover_uri")
    val coverUri: String?,  // Путь к файлу изображения для обложки
    @ColumnInfo(name = "track_ids_list")
    val trackIdsList: String, // Список идентификаторов треков, которые будут добавляться в этот плейлист(gson)
    @ColumnInfo(name = "tracks_count")
    val tracksCount: Int, // Количество треков
)
