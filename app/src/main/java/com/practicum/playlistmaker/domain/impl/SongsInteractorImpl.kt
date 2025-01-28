package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SongRepository
import com.practicum.playlistmaker.domain.api.SongsInteractor
import java.util.concurrent.Executors


class SongsInteractorImpl (private val repository: SongRepository) : SongsInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchSongs(expression: String, foundConsumer: SongsInteractor.SongsConsumer) {
       executor.execute {
           foundConsumer.consume(repository.searchSongs(expression))
       }
    }
}