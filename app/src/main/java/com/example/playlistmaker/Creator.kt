package com.example.playlistmaker

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.example.playlistmaker.data.repositories.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackCoverRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TrackCoverRepository
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.GetTrackByIdUseCase
import com.example.playlistmaker.domain.usecases.GetTrackCoverUseCase

object Creator {
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


    private fun getTrackByIdRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(context)
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