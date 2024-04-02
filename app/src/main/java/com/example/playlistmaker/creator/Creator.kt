package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient

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
        url: String?,
        statusObserver: PlayerRepositoryImpl.StatusObserver
    ): PlayerRepository {
        return PlayerRepositoryImpl(context, url, statusObserver)
    }

    fun providePlayerInteractor(
        context: Context,
        url: String?,
        statusObserver: PlayerRepositoryImpl.StatusObserver
    ): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(context, url, statusObserver))
    }


    private fun getTrackByIdRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideTrackByIdUseCase(context: Context): GetTrackByIdUseCase {
        return GetTrackByIdUseCase(getTrackByIdRepository(context))
    }

}