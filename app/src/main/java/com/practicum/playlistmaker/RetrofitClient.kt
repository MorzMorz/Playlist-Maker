package com.practicum.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val ITunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesService = retrofit.create(ITunesSearchApi::class.java)
}