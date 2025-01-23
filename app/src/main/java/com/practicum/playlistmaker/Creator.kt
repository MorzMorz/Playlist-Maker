package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.SongsRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.SongRepository
import com.practicum.playlistmaker.domain.api.SongsInteractor
import com.practicum.playlistmaker.domain.impl.SongsInteractorImpl

object Creator {
    private fun getSongsRepository(): SongRepository {
        return SongsRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }

}