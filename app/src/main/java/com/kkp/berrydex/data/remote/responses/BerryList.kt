package com.kkp.berrydex.data.remote.responses

data class BerryList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)