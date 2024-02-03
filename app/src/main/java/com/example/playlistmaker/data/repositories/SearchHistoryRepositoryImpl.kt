package com.example.playlistmaker.data.repositories

import android.content.Context
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.settings.activity.PLAYLIST_MAKER_PREFERENCES
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "key_for_search_history"

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCES,
        Context.MODE_PRIVATE
    )
    private val tracks = getHistory()

    override fun addTrack(track: Track) {
        for (i in 0 until tracks.size) {
            if (track.trackId == tracks[i].trackId) {
                tracks.removeAt(i)
                break
            }
        }
        if (tracks.size == 10) tracks.removeLast()
        tracks.add(0, track)
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override fun getHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList<Track>()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun clearHistory() {
        tracks.clear()
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
}