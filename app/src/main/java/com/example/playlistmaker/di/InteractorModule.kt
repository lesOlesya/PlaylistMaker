package com.example.playlistmaker.di

import com.example.playlistmaker.creating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.creating_playlist.domain.PlaylistCoverInteractorImpl
import com.example.playlistmaker.media.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.PlaylistsInteractor
import com.example.playlistmaker.media.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SettingsInteractor> {
       SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    factory<GetTrackByIdUseCase> {
        GetTrackByIdUseCase(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

    factory<PlaylistCoverInteractor> {
        PlaylistCoverInteractorImpl(get())
    }

}