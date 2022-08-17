package com.kkp.berrydex.berrydetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kkp.berrydex.data.remote.responses.Berry
import com.kkp.berrydex.util.Resource

@Composable
fun BerryDetailScreen(
    dominantColor : Color,
    berryName : String,
    berryId : Int,
    navController: NavController,
    viewModel : BerryDetailViewModel = hiltViewModel()
){
    val berryInfo = produceState<Resource<Berry>>(initialValue = Resource.Loading()){
        value = viewModel.getBerryInfo(berryName)
    }.value

    val berryURL = "https://raw.githubusercontent.com/michalbialacki/BerrySprites/main/Berries/${berryId}.png"
    BerryStateWrapper(berryInfo = berryInfo)



    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(),
        contentAlignment = Alignment.Center){
        BerryStateWrapper(berryInfo = berryInfo)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            if (berryInfo is Resource.Success){
                AsyncImage(
                    model = berryURL,
                    contentDescription = berryName,
                    modifier = Modifier
                        .size(80.dp)
                        .offset(y = 20.dp)
                )
            }
        }

    }
}

@Composable
fun BerryStateWrapper(
    berryInfo: Resource<Berry>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(berryInfo){
        is Resource.Success ->{
//            BerrySpec(berryInfo = berryInfo.data!!)
            BerryCard(berryInfo = berryInfo.data!!)
        }
        is Resource.Loading ->{
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
        is Resource.Error -> {
            Text(
                text = berryInfo.message!!,
                color = Color.Red,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun BerryCard(
    berryInfo: Berry,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier
        .padding(
            top = 60.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .offset(y = 40.dp)
                .fillMaxSize(),


        ) {
            Text(
                text = berryInfo.name.replaceFirstChar { it.uppercase() } + " Berry",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))

            BerryNatGift(berryInfo = berryInfo)

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))

            BerryFlavorWrapper(berryInfo = berryInfo)

        }
    }


}

@Composable
fun BerrySpec(
    berryInfo : Berry,
    animDelayPerItem : Int = 100,
) {
    var tasteList = mutableListOf<List<String>>()
    berryInfo.flavors.let {
        for (i in it.indices){
            tasteList.add(listOf(
                it.get(i).flavor.name.replaceFirstChar(Char::titlecase),
                it.get(i).potency.toString())
            )
        }
        Log.d("Detail", "BerryDetailScreen: ${tasteList}")
    }

}

@Composable
fun BerryNatGift(
    berryInfo: Berry
) {
    Box(modifier = Modifier
        .width(300.dp)
        .height(120.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.LightGray)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Natural Gift",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface)
            NatGiftStats(
                statName = "Type",
                statValue = berryInfo.natural_gift_type.name.replaceFirstChar { it.uppercase() }
            )
            NatGiftStats(statName = "Power", statValue = berryInfo.natural_gift_power.toString())

        }

    }

}

@Composable
fun NatGiftStats(
    statName : String,
    statValue : String
) {
    Row(
        modifier = Modifier
            .height(35.dp)
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Color.DarkGray
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = statName,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(Color.LightGray)
                .clip(RoundedCornerShape(10.dp)),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Text(
            text = statValue,
            modifier = Modifier
                .fillMaxWidth(0.5f),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }

}

@Composable
fun BerryFlavorWrapper(
    berryInfo: Berry
) {
    Box(modifier = Modifier
        .width(300.dp)
        .height(420.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.LightGray)) {

    }

}

@Composable
fun BerryFlavourSpec(
    flavorName : String,
    flavorPotency : Int,
    statColor : Color = Color.LightGray
) {


}