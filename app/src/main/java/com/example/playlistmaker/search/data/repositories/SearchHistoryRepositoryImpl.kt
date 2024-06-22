package com.example.playlistmaker.search.data.repositories

import android.content.SharedPreferences
import com.example.playlistmaker.media.data.AppDatabase
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

const val SEARCH_HISTORY_KEY = "key_for_search_history"

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : SearchHistoryRepository {

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
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }


    override fun getHistory(): ArrayList<Track> {
        val favoriteTracksId = GlobalScope.async { appDatabase.favoriteTrackDao().getTracksId() }
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList<Track>()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val tracks = gson.fromJson<ArrayList<Track>>(json, type)
        GlobalScope.launch { tracks.map {
            it.isFavorite = it.trackId in favoriteTracksId.await()
        } }
        return tracks
    }

    override fun clearHistory() {
        tracks.clear()
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
}