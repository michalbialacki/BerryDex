package com.kkp.berrydex.data.remote.responses

data class Berry(
    val firmness: Firmness,
    val flavors: List<Flavor>,
    val growth_time: Int,
    val id: Int,
    val item: Item,
    val max_harvest: Int,
    val name: String,
    val natural_gift_power: Int,
    val natural_gift_type: NaturalGiftType,
    val size: Int,
    val smoothness: Int,
    val soil_dryness: Int
)