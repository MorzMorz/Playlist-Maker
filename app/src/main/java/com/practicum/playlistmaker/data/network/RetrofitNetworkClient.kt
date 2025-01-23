package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SongSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private  val ITunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SongSearchRequest){
            val resp = iTunesService.search(dto.searchRequest).execute()
            val body = resp.body() ?: Response()
            return body.apply{ resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}