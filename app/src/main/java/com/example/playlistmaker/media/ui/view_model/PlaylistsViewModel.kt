package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.model.Playlist

//class PlaylistsViewModel(private var playlistsInteractor : PlaylistsInteractor) : ViewModel() {
class PlaylistsViewModel : ViewModel() {

    //    private val playlistsLiveData = MutableLiveData(PlaylistsInteractor.getTracks())
    private val playlistsLiveData = MutableLiveData(ArrayList<Playlist>())

    fun getFavoriteTracksLiveData(): LiveData<ArrayList<Playlist>> = playlistsLiveData
}