package com.atriadha99.noctra.domain.source

data class SourceCapabilities(
    val canStreamDirectly: Boolean,
    val supportsCustomDsp: Boolean, // crossfade, eq, dsb
    val requiresHostAppRunning: Boolean
)
