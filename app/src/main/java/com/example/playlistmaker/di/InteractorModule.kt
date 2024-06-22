package com.example.playlistmaker.di

import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.creating_and_updating_playlist.domain.impl.PlaylistCoverInteractorImpl
import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.playlists.domain.PlaylistsInteractor
import com.example.playlistmaker.media.favoriteTracks.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.playlists.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.player.domain.impl.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
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