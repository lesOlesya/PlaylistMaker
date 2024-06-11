package com.example.playlistmaker.media.domain.model

import com.example.playlistmaker.search.domain.models.Track

data class Playlist(
    val playlistId: Long,
    val playlistName: String, // Название плейлиста
    val playlistDescription: String?, // Описание плейлиста
    val coverUri: String?,  // Путь к файлу изображения для обложки
    val tracksList: ArrayList<Track>, // Список идентификаторов треков, которые будут добавляться в этот плейлист
    var tracksCount: Int, // Количество треков
)
