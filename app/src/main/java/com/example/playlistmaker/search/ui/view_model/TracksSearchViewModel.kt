package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.TracksState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistory: SearchHistoryInteractor
) : ViewModel() {

    private val tracks = ArrayList<Track>()

    private var latestSearchText: String? = null
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            findTracks(changedText)
        }

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

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText

            trackSearchDebounce(changedText)
        }
    }

    fun updateDebounce() {
        latestSearchText?.let { findTracks(latestSearchText!!) }
    }

    private fun findTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(TracksState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorCode: Int?) {
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

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}