package com.devapps.showsapp

data class ShowResponse(
    val total: String,
    val page: Int,
    val pages: Int,
    val tv_shows: List<TvShowItem>
)

data class TvShowItem(
    var id: Int? = null,
    var name: String? = null,
    var permalink: String? = null,
    var start_date: String? = null,
    var end_date: String? = null,
    var country: String? = null,
    var network: String? = null,
    var status: String? = null,
    var image_thumbnail_path: String? = null
){
    constructor() : this(null, null, null, null, null, null, null, null, null)
}