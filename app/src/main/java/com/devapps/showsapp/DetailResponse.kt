package com.devapps.showsapp

data class DetailResponse(
    val tvShow: ShowDetailItem
)

data class ShowDetailItem(
    val id: Int,
    val name: String,
    val permalink: String,
    val url: String,
    val description: String,
    val description_source: String,
    val start_date: String,
    val end_date: String?,
    val country: String,
    val status: String,
    val runtime: Int,
    val network: String,
    val youtube_link: String?,
    val image_path: String,
    val image_thumbnail_path: String,
    val rating: String,
    val rating_count: String,
    val countdown: String?,
    val genres: List<String>,
    val pictures: List<String>,
    val episodes: List<Episode>
)

data class Episode(
    val season: Int,
    val episode: Int,
    val name: String,
    val air_date: String
)