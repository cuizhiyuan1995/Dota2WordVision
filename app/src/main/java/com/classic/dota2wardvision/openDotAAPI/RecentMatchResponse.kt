package com.classic.dota2wardvision.openDotAAPI


data class RecentMatchResponse(
    val match_id: Long,
    val hero_id: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val duration: Int,
    val start_time: Long
)

