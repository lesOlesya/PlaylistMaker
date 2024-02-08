package com.example.playlistmaker.search.ui.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.ui.models.TracksState

class TracksSearchViewModel(application: Application): AndroidViewModel(application)  {

    private val tracksInteractor by lazy { Creator.provideTracksInteractor(getApplication<Application>()) }
    private val handler = Handler(Looper.getMainLooper())

    private val tracks = ArrayList<Track>()

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        if (newSearchText.isNotEmpty()) findTracks(newSearchText)
    }

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

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

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

}