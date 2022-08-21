package com.kkp.berrydex.berrydetail

import android.util.Log
import android.widget.Space
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kkp.berrydex.R
import com.kkp.berrydex.data.remote.responses.*
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
    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xff47cba3),
            Color(0xffb5ffff),
            Color(0xff47cba3)
        )
    )
    val berryURL = "https://raw.githubusercontent.com/michalbialacki/BerrySprites/main/Berries/${berryId}.png"
    BerryStateWrapper(berryInfo = berryInfo)



    Box(modifier = Modifier
        .fillMaxSize()
        .background(gradient)
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
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1
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
                style = MaterialTheme.typography.h1
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
    var rotated by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(700))
    val frontAnimation by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(700)
    )
    val backAnimation by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(700)
    )

    Box(modifier = Modifier
        .width(300.dp)
        .height(120.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colors.primary)
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 8 * density
        }
        .clickable {
            rotated = !rotated
        }){
        Column(
            modifier = Modifier.fillMaxSize()
                .graphicsLayer {
                    alpha = if (rotated) backAnimation else frontAnimation
                    rotationY = rotation
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!rotated){
                Text(
                    text = "Natural Gift",
                    style = MaterialTheme.typography.h2
                )
                NatGiftStats(
                    statName = "Type",
                    statValue = berryInfo.natural_gift_type.name.replaceFirstChar { it.uppercase() }
                )
                NatGiftStats(
                    statName = "Power",
                    statValue = berryInfo.natural_gift_power
                        .toString()
                )
            }
            else{
                val berryFirmness = berryInfo.firmness.name.replace('-',' ')

                val berryTrivia = "This berry feels ${berryFirmness} in your hand. " +
                        "It measures about ${berryInfo.size / 10} centimeters. " +
                        "You may gather them every ${4 * berryInfo.growth_time} hours " +
                        "and - if cared properly - it can grow up to ${berryInfo.max_harvest}" +
                        " berries per tree."
                Text(
                    text = "Usefull Info",
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = "${berryTrivia} ",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 4.dp
                    )
                )
            }
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
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(35.dp)
                .background(MaterialTheme.colors.secondaryVariant)
                .clip(RoundedCornerShape(10.dp)),

        )
        Text(
            text = statValue,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(35.dp)
                .background(MaterialTheme.colors.secondary),

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
        .background(MaterialTheme.colors.primary)) {
        BerryFlavorSection(berryInfo = berryInfo)

    }

}

@Composable
fun BerryFlavorSection(
    berryInfo: Berry,
    animDelayPerItem: Int = 100
) {
    val maxFlavorValue = remember {
        berryInfo.flavors.maxOf { it.potency }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        Text(
            text = "Flavor",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(
                start = 4.dp,
                end = 6.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween) {
            for (i in berryInfo.flavors.indices){
                val flavor = berryInfo.flavors[i]
                BerryFlavourSpec(
                    flavorName = flavor.flavor.name,
                    flavorPotency = flavor.potency,
                    flavorPotencyMax = maxFlavorValue
                )
            }
        }


    }

}

@Composable
fun BerryFlavourSpec(
    flavorName : String,
    flavorPotency : Int,
    flavorPotencyMax: Int,
    statColor : Color = Color.Blue
) {
    val barValue = flavorPotency / (1.25f * flavorPotencyMax)
    var animationPlayed by remember{
        mutableStateOf(false)
    }

    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed){
            flavorPotency / (1.25f * flavorPotencyMax.toFloat())
        } else 0f,
        animationSpec = tween(
            1000,
            0
        )
    )
    LaunchedEffect(key1 = true){
        animationPlayed = true
    }


    Box(contentAlignment = Alignment.BottomCenter){
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(0.9f)
                .clip(CircleShape)
                .alpha(0.7f)
                .background(MaterialTheme.colors.onError)
                .padding(8.dp),
            contentAlignment = Alignment.BottomCenter){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(curPercent.value)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface)
            ) {
                Text(
                    text = flavorPotency.toString(),
                    style = MaterialTheme.typography.h3
                )

            }

        }
        Text(
            text = flavorName.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.h3,
            modifier = Modifier.offset(y=30.dp))
    }






}
val previewFlavorList = listOf<Flavor>(
    Flavor(FlavorX("spicy","URL"),0),
    Flavor(FlavorX("dry","URL"),0),
    Flavor(FlavorX("sweet","URL"),5),
    Flavor(FlavorX("bitter","URL"),0),
    Flavor(FlavorX("sour","URL"),5)
    )

val previewBerry = Berry(
    firmness = Firmness("Firm","URL"),
    flavors = previewFlavorList,
    growth_time = 12,
    id = 66,
    item = Item("Item","URL"),
    max_harvest = 15,
    name = "Berry",
    natural_gift_power = 60,
    natural_gift_type = NaturalGiftType("water","url"),
    size = 1,
    smoothness = 2,
    soil_dryness = 1
)

@Preview
@Composable
fun myPreview() {
    BerryCard(berryInfo = previewBerry)
//        BerryFlavourSpec(flavorName = "Dry", flavorPotency = 10, flavorPotencyMax = 10)
}