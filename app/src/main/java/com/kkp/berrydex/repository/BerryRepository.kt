package com.kkp.berrydex.repository

import com.kkp.berrydex.data.remote.BerryApi
import com.kkp.berrydex.data.remote.responses.BerryList
import com.kkp.berrydex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped //lives as long as actvity

class BerryRepository @Inject constructor(
    private val api : BerryApi
) {
    suspend fun getBerryList(limit : Int, offset : Int) : Resource<BerryList>{
        val response = try {
            api.getBerryList(limit, offset)
        } catch (e:Exception){
            return Resource.Error("Error unknown")
        }
        return Resource.Success(response)
    }
}