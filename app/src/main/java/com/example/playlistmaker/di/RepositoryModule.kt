package com.example.playlistmaker.di

import com.example.playlistmaker.creating_and_updating_playlist.data.PlaylistCoverRepositoryImpl
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverRepository
import com.example.playlistmaker.media.favoriteTracks.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.media.playlists.data.PlaylistsRepositoryImpl
import com.example.playlistmaker.media.playlists.data.convertor.PlaylistDbConvertor
import com.example.playlistmaker.media.favoriteTracks.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.favoriteTracks.domain.FavoriteTracksRepository
import com.example.playlistmaker.media.playlists.domain.PlaylistsRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<PlaylistCoverRepository> {
        PlaylistCoverRepositoryImpl(androidContext())
    }


    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    factory { TrackDbConvertor() }

    factory { PlaylistDbConvertor(get()) }

}