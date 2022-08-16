package com.kkp.berrydex.berrylist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.kkp.berrydex.repository.BerryRepository
import javax.inject.Inject

class BerryListViewModel @Inject constructor(
    private val repository: BerryRepository
): ViewModel() {

    private val curPage = 0
    var berryList = mutableStateListOf<List<BerryList>>()
}