package com.kkp.berrydex.berrydetail

import androidx.lifecycle.ViewModel
import com.kkp.berrydex.data.remote.responses.Berry
import com.kkp.berrydex.repository.BerryRepository
import com.kkp.berrydex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BerryDetailViewModel @Inject constructor(
    private val repository: BerryRepository
): ViewModel() {
    suspend fun getBerryInfo(berryName : String) : Resource<Berry>{
        return repository.getBerryInfo(berryName)
    }
}