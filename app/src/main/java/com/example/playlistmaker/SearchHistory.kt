package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "key_for_search_history"

class SearchHistory (
    private val sharedPreferences: SharedPreferences
) {

    private val tracks = getHistory()

    fun addTrack(track: Track) {
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

    fun getHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList<Track>()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clearHistory() {
        tracks.clear()
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }


}