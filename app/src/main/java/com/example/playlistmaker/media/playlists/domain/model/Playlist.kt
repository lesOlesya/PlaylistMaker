package com.example.playlistmaker.media.playlists.domain.model

data class Playlist(
    val playlistId: Int,
    val timeOfUpdate: Long, // время обновления для сохранения обложки
    val playlistName: String, // Название плейлиста
    val playlistDescription: String?, // Описание плейлиста
    val coverUri: String?,  // Путь к файлу изображения для обложки
    val trackIdsList: ArrayList<Int>, // Список идентификаторов треков, которые будут добавляться в этот плейлист
    var tracksCount: Int, // Количество треков
)
