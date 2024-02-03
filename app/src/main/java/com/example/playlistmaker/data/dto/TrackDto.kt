package com.example.playlistmaker.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String,  // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна исполнителя
    val previewUrl: String?, // Ссылка на сниппет
) {
//    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
//    fun getTrackTime() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)!!

}
