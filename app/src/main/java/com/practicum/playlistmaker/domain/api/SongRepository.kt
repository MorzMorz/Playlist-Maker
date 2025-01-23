package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.data.dto.SongResponse
import com.practicum.playlistmaker.data.dto.SongSearchRequest
import com.practicum.playlistmaker.domain.models.SearchedSongsResponse

interface SongRepository {
    fun searchSongs(searchRequest: String): SearchedSongsResponse
}