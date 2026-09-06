package com.atriadha99.noctra.domain.model

data class Track(
    val id: String,
    val sourceId: String,
    val title: String,
    val artist: String,
    val artworkUrl: String? = null,
    val durationMs: Long = 0L,
    val streamUrl: String = "",
    val coverUrl: String? = artworkUrl
)
