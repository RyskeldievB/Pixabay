package com.geektech.pinterestwithpixabay

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class
RetrofitServices {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://pixabay.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var api : PixaApi = retrofit.create(PixaApi::class.java)
}
