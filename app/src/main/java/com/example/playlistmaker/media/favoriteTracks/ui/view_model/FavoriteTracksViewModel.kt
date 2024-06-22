package com.example.playlistmaker.media.favoriteTracks.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.favoriteTracks.ui.state.FavoriteTracksState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private var favoriteTracksInteractor : FavoriteTracksInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun getStateLiveData(): LiveData<FavoriteTracksState> = stateLiveData

    init {
        onResume()
    }

    fun onResume() {
        viewModelScope.launch {
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { it ->
                    processResult(it)
                }
        }
    }

    private fun processResult(favoriteTracks: List<Track>) {
        if (favoriteTracks.isEmpty()) renderState(FavoriteTracksState.Empty(0))
        else renderState(FavoriteTracksState.Content(favoriteTracks))
    }

    private fun renderState(state: FavoriteTracksState) {
        stateLiveData.postValue(state)
    }
}