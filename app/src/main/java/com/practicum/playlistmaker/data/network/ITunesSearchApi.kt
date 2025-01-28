package com.practicum.playlistmaker.data.network


import com.practicum.playlistmaker.data.dto.SongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi{
    @GET("/search?entity=song")
    fun search(@Query("term", encoded = false) text: String): Call<SongResponse>
}