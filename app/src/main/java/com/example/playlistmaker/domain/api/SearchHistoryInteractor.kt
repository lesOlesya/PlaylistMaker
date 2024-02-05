package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getHistory(): ArrayList<Track>

    fun clearHistory()
}