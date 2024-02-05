package com.example.playlistmaker.creator

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repositories.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackCoverRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TrackCoverRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.GetTrackByIdUseCase
import com.example.playlistmaker.domain.usecases.GetTrackCoverUseCase
import com.example.playlistmaker.presentation.ui.search.activity.TrackAdapter
import com.example.playlistmaker.presentation.ui.search.view_model.TracksSearchPresenter
import com.example.playlistmaker.presentation.ui.search.view_model.TracksView

object Creator {


    fun provideTracksSearchPresenter(
        context: Context
    ): TracksSearchPresenter {
        return TracksSearchPresenter(
            context = context
        )
    }


    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }


    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }


    private fun getPlayerRepository(
        context: Context,
        play: ToggleButton,
        tvTrackTime: TextView,
        url: String?
    ): PlayerRepository {
        return PlayerRepositoryImpl(context, play, tvTrackTime, url)
    }

    fun providePlayerInteractor(
        context: Context,
        play: ToggleButton,
        tvTrackTime: TextView,
        url: String?
    ): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(context, play, tvTrackTime, url))
    }


    private fun getTrackByIdRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideTrackByIdUseCase(context: Context): GetTrackByIdUseCase {
        return GetTrackByIdUseCase(getTrackByIdRepository(context))
    }


    private fun getTrackCoverRepository(
        context: Context,
        track: Track,
        placeForCover: ImageView
    ): TrackCoverRepository {
        return TrackCoverRepositoryImpl(context, track, placeForCover)
    }

    fun provideTrackCoverUseCase(
        context: Context,
        track: Track,
        placeForCover: ImageView
    ): GetTrackCoverUseCase {
        return GetTrackCoverUseCase(getTrackCoverRepository(context, track, placeForCover))
    }
}