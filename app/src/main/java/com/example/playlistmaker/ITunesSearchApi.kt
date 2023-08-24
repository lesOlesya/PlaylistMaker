package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {
    @GET("/search")
    fun search(@Query("term") text: String) : Call<TracksResponse>
}
//?entity=song