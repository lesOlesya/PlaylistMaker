package com.example.playlistmaker.data.repositories

import android.content.Context
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.settings.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.presentation.ui.tracks.SEARCH_HISTORY_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TracksRepositoryImpl(context: Context) : TracksRepository {

    val sharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES,
        Context.MODE_PRIVATE
    )

    override fun getTracks(): List<Track> {

        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val tracks: ArrayList<Track> = Gson().fromJson(json, type)
        return tracks
    }
}