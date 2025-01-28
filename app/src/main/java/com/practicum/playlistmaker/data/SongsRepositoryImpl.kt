package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.SongResponse
import com.practicum.playlistmaker.data.dto.SongSearchRequest
import com.practicum.playlistmaker.domain.api.SongRepository
import com.practicum.playlistmaker.domain.models.SearchedSongsResponse
import com.practicum.playlistmaker.domain.models.Song

class SongsRepositoryImpl (private val networkClient: NetworkClient) : SongRepository {

override fun searchSongs(searchRequest: String): SearchedSongsResponse{
    val response = networkClient.doRequest(SongSearchRequest(searchRequest))
    val searchedSongsResponse = SearchedSongsResponse(response.resultCode)
    if (response.resultCode == 200) {
        searchedSongsResponse.trackList = (response as SongResponse).results.map {
            Song(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
    }
    return searchedSongsResponse
}

}