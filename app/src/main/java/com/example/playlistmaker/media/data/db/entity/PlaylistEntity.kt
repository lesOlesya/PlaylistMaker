package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey //(autoGenerate = true)
    val playlistId: Long,
    val playlistName: String, // Название плейлиста
    val playlistDescription: String?, // Описание плейлиста
    val coverUri: String?,  // Путь к файлу изображения для обложки
    val tracksList: String, // Список идентификаторов треков, которые будут добавляться в этот плейлист(gson)
    val tracksCount: Int, // Количество треков
)
