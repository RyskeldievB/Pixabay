package com.geektech.pinterestwithpixabay.model

data class PixaModel(
    var hits: ArrayList<HitsModel>
)

data class HitsModel(
    var largeImageURL: String,
    var tags: String,
)

// TODO:
//        "downloads": 6439,
//        "likes": 5,
