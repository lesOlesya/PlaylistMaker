package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)

    fun getHistory(): ArrayList<Track>

    fun clearHistory()
}