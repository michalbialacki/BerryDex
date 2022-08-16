package com.kkp.berrydex.data.remote

import com.kkp.berrydex.data.remote.responses.Berry
import com.kkp.berrydex.data.remote.responses.BerryList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BerryApi {
    @GET("berry")
    suspend fun getBerryList(
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : BerryList

    @GET("berry/{name}")
    suspend fun getBerryInfo(
        @Path("name") name: String
    ) : Berry
}