package com.classic.dota2wardvision.openDotAAPI

data class WardMapResponse(
    val obs: Map<String, Map<String, Int>>?,   // observer wards placed { "123:456": count }
    val sen: Map<String, Map<String, Int>>?    // sentry wards placed
)
