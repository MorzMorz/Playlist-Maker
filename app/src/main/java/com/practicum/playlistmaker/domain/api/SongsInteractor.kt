package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchedSongsResponse

interface SongsInteractor {


    fun searchSongs(
        expression: String,
        foundConsumer: SongsConsumer
    )

    interface SongsConsumer{
        fun consume(foundSongs: SearchedSongsResponse)
    }
}