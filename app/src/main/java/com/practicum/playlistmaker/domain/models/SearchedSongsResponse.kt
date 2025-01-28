package com.practicum.playlistmaker.domain.models

class SearchedSongsResponse (
    val resultCode: Int
) {
    var trackList = emptyList<Song>()
}