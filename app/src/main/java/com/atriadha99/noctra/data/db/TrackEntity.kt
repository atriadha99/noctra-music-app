package com.atriadha99.noctra.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val mediaUri: String,
    val artworkUri: String?,
    val isFavorite: Boolean = false,
    val source: String = "LOCAL", // LOCAL, SPOTIFY, APPLE, YOUTUBE
    val addedAt: Long = System.currentTimeMillis()
)
