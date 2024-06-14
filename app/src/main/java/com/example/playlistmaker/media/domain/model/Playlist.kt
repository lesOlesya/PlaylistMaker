package com.example.playlistmaker.media.domain.model

data class Playlist(
    val playlistId: Long,
    val playlistName: String, // Название плейлиста
    val playlistDescription: String?, // Описание плейлиста
    val coverUri: String?,  // Путь к файлу изображения для обложки
    val tracksList: ArrayList<Int>, // Список идентификаторов треков, которые будут добавляться в этот плейлист
    var tracksCount: Int, // Количество треков
)
