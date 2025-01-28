package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Song

 fun DtoToSong(dtoList: List<SongDto>): List<Song> {
    return dtoList.map { dto ->
        Song(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl
        )
    }
}