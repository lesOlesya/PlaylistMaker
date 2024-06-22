package com.example.playlistmaker.di

import com.example.playlistmaker.creating_and_updating_playlist.ui.view_model.CreatingPlaylistViewModel
import com.example.playlistmaker.creating_and_updating_playlist.ui.view_model.UpdatingPlaylistViewModel
import com.example.playlistmaker.root.ui.RootViewModel
import com.example.playlistmaker.media.favoriteTracks.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.playlist.ui.PlaylistInfoViewModel
import com.example.playlistmaker.search.ui.view_model.TracksSearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        RootViewModel(get())
    }

    viewModel {
        TracksSearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (trackId: Int) ->
        PlayerViewModel(trackId, get(), get(), get(), get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatingPlaylistViewModel(get(), get())
    }

    viewModel { (playlistId: Int) ->
        PlaylistInfoViewModel(playlistId, get(), get(), get())
    }

    viewModel { (playlistId: Int) ->
        UpdatingPlaylistViewModel(playlistId, get(), get())
    }
}