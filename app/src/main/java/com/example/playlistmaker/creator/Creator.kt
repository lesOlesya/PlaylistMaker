package com.example.playlistmaker.creator

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.player.data.TrackCoverRepositoryImpl
import com.example.playlistmaker.search.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.player.domain.TrackCoverRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.GetTrackCoverUseCase

object Creator {

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