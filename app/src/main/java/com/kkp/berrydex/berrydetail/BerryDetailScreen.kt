package com.kkp.berrydex.berrydetail

import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.kkp.berrydex.data.remote.responses.*
import com.kkp.berrydex.util.Resource
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun BerryDetailScreen(
    berryName: String,
    berryId: Int,
    viewModel: BerryDetailViewModel = hiltViewModel()
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
    loadingModifier: Modifier = Modifier
) {
    when(berryInfo){
        is Resource.Success ->{
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
    berryInfo: Berry
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
            modifier = Modifier
                .fillMaxSize()
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

                val berryTrivia = "This berry feels $berryFirmness in your hand. " +
                        "It measures about ${berryInfo.size / 10} centimeters. " +
                        "You may gather them every ${4 * berryInfo.growth_time} hours " +
                        "and - if cared properly - it can grow up to ${berryInfo.max_harvest}" +
                        " berries per tree."
                Text(
                    text = "Usefull Info",
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = "$berryTrivia ",
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
    berryInfo: Berry
) {

    var rotated by remember {
        mutableStateOf(false)
    }
    val frontAnimation by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(700)
    )
    val backAnimation by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(700)
    )


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { rotated = !rotated }
                    )
                }
                .graphicsLayer {
                    alpha = if (rotated) backAnimation else frontAnimation
                }
        ) {
            if (!rotated){
                FlavorPage(berryInfo = berryInfo)
            }
            else{
                PlantBerry(berryInfo = berryInfo)
            }
        }


}

@Composable
fun FlavorPage(
    berryInfo: Berry
) {

    val maxFlavorValue = remember {
        berryInfo.flavors.maxOf { it.potency }
    }

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

@Composable
fun PlantBerry(
    berryInfo: Berry
) {
    val thisContext = LocalContext.current

    val timeStamp = DateTimeFormatter
        .ofPattern("yyyyMMddHHmmss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
    val berryBitmap = createQR(berryInfo, timeStamp)


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)) {
                Text(text = "Hey there, farmer!",
                    style = MaterialTheme.typography.h2)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            val file = File(
                                Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .toString(),
                                "${berryInfo.name}${timeStamp}"+".jpg")
                            val fOut = FileOutputStream(file)
                            berryBitmap.compress(Bitmap.CompressFormat.JPEG,85,fOut)
                            fOut.flush()
                            fOut.close()
                            Toast.makeText(thisContext,"QR saved!",Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            ){
                Image(bitmap = berryBitmap.asImageBitmap(),
                    contentDescription = "QR for new berry",
                )
//                Text(text = "XD",modifier = Modifier.clickable {

//                })
            }

            Text(text = "If you plan to plant a new berry tree, feel free to tag it" +
                    "with this QR code. You will find it much easier to monitor and" +
                    "to harvest the fruits of your patience! Press and hold the code to save" +
                    " it to print it later!",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BerryFlavourSpec(
    flavorName: String,
    flavorPotency: Int,
    flavorPotencyMax: Int
) {
    flavorPotency / (1.25f * flavorPotencyMax)
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
val previewFlavorList = listOf(
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
fun MyPreview() {
    BerryCard(berryInfo = previewBerry)
//        BerryFlavourSpec(flavorName = "Dry", flavorPotency = 10, flavorPotencyMax = 10)
}

fun createQR(
    berryInfo: Berry,
    timeStamp : String) : Bitmap{
    val berryFlavor = berryInfo.flavors

    val flavMap = mapOf(
        berryFlavor[0].flavor.name to berryFlavor[0].potency.toString(),
        berryFlavor[1].flavor.name to berryFlavor[1].potency.toString(),
        berryFlavor[2].flavor.name to berryFlavor[2].potency.toString(),
        berryFlavor[3].flavor.name to berryFlavor[3].potency.toString(),
        berryFlavor[4].flavor.name to berryFlavor[4].potency.toString(),

        )
    val infoToBitmap = "BerryFarmerApplication:${berryInfo.name.uppercase()} Berry;${flavMap};$timeStamp;" +
            " ${berryInfo.growth_time};${berryInfo.max_harvest}"
    val bitMatrix = QRCodeWriter().encode(infoToBitmap,BarcodeFormat.QR_CODE, 512,512)
    val berryBitmap = Bitmap.createBitmap(512,512,Bitmap.Config.RGB_565)
    for (x in 0 until bitMatrix.width){
        for (y in 0 until bitMatrix.height){
            berryBitmap.setPixel(x,y, if (bitMatrix.get(x,y)) Color.Black.toArgb() else Color.White.toArgb())
        }
    }
    return berryBitmap
}
