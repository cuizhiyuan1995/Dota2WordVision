package com.classic.dota2wardvision.openDotAAPI

data class ResolveVanityResponse(
    val response: Response
) {
    data class Response(
        val success: Int,
        val steamid: String?
    )
}