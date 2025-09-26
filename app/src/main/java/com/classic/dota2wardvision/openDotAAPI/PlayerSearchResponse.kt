package com.classic.dota2wardvision.openDotAAPI

data class PlayerSearchResponse(
    val account_id: Long,
    val personaname: String?,
    val avatarfull: String?,
    val last_match_time: String?
)