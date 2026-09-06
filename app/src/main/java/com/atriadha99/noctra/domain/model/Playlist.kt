package com.atriadha99.noctra.domain.model

data class Playlist(
    val id: String,
    val sourceId: String,
    val title: String,
    val description: String?,
    val coverUrl: String?,
    val tracks: List<Track>
)
