package com.geektech.pinterestwithpixabay

import com.geektech.pinterestwithpixabay.model.PixaModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaApi {
    @GET("api/")
    fun searchImage(
        @Query("q") keyWord: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = 20,
        @Query("key") key: String = "20524823-aaf2aa3104218fc15088e7af3",
    ): Call<PixaModel>
}