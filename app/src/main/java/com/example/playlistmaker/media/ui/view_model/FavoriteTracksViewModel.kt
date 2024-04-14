package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

//class FavoriteTracksViewModel(private var favoriteTracksInteractor : FavoriteTracksInteractor) : ViewModel() {
class FavoriteTracksViewModel : ViewModel() {

//    private val favoriteTracksLiveData = MutableLiveData(favoriteTracksInteractor.getTracks())
    private val favoriteTracksLiveData = MutableLiveData(ArrayList<Track>())

    fun getFavoriteTracksLiveData(): LiveData<ArrayList<Track>> = favoriteTracksLiveData
}