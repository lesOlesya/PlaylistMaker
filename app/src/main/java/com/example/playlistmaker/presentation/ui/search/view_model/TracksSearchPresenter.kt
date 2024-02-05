package com.example.playlistmaker.presentation.ui.search.view_model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.search.activity.SearchActivity
import com.example.playlistmaker.presentation.ui.search.activity.TrackAdapter
import com.example.playlistmaker.presentation.ui.search.models.TracksState
import moxy.MvpPresenter

class TracksSearchPresenter(
    private val context: Context
) : MvpPresenter<TracksView>() {

    private val tracksInteractor by lazy { Creator.provideTracksInteractor(context) }

    private val tracks = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        if (newSearchText.isNotEmpty()) findTracks(newSearchText)
    }

    private var latestSearchText: String? = null

    private fun findTracks(query: String) {
        renderState(TracksState.Loading)
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: List<Track>?, errorCode: Int?) {
                handler.post {
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
        viewState.render(state)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}