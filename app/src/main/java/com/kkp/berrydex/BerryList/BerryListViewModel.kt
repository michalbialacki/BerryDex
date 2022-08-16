package com.kkp.berrydex.BerryList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.kkp.berrydex.data.models.BerryListEntry
import com.kkp.berrydex.repository.BerryRepository
import com.kkp.berrydex.util.Constant.PAGE_SIZE
import com.kkp.berrydex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BerryListViewModel @Inject constructor(
    private val repository: BerryRepository
): ViewModel() {

    private var curPage = 0
    var berryList = mutableStateOf<List<BerryListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    private var cachedBerryList = listOf<BerryListEntry>()
    private var isSearchStarted = true
    var isSearching = mutableStateOf(false)

    init {
        loadBerriesPaginated()
    }





    fun loadBerriesPaginated(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getBerryList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result){
                is Resource.Success ->{
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val berriesEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        var url = "https://raw.githubusercontent.com/michalbialacki/BerrySprites/main/Berries/${number}.png"
                        BerryListEntry(entry.name.capitalize(Locale.ROOT),url)
                    }
                    curPage++
                    Log.d("VIEWMODEL", "${curPage}")
                    loadError.value = ""
                    isLoading.value = false
                    berryList.value += berriesEntries
                }
                is Resource.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit){
        var bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate{ pallete->
            pallete?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}