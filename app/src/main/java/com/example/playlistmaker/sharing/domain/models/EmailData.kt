package com.example.playlistmaker.sharing.domain.models

data class EmailData
    (
    val email: Array<String>,
    val subject: String,
    val text: String
) {
}