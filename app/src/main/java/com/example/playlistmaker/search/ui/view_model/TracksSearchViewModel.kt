package com.example.playlistmaker.search.ui.view_model

import android.annotation.SuppressLint
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.TracksState

class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistory: SearchHistoryInteractor,
    private val handler : Handler
) : ViewModel() {

    private val tracks = ArrayList<Track>()

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        if (newSearchText.isNotEmpty()) findTracks(newSearchText)
    }

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    private val searchHistoryLiveData = MutableLiveData(searchHistory.getHistory())

    fun getStateLiveData(): LiveData<TracksState> = stateLiveData
    fun getSearchHistoryLiveData(): LiveData<ArrayList<Track>> = searchHistoryLiveData

    fun clearSearchHistory() {
        searchHistory.clearHistory()
        searchHistoryLiveData.postValue(searchHistory.getHistory())
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistory.addTrack(track)
        searchHistoryLiveData.postValue(searchHistory.getHistory())
    }

    private fun findTracks(query: String) {
        renderState(TracksState.Loading)
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: List<Track>?, errorCode: Int?) {
                if (foundTracks != null) {
                    tracks.clear()
                    tracks.addAll(foundTracks)
                }

                when {
                    errorCode != null -> {
                        renderState(TracksState.Error(errorCode))
                    }

                    tracks.isEmpty() -> {
                        renderState(TracksState.Empty(0))
                    }

                    else -> {
                        renderState(TracksState.Content(tracks))
                    }
                }
            }
        })
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        this.lastSearchText = changedText
        updateDebounce()
    }

    fun updateDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}