package com.kkp.berrydex.BerryList

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import com.kkp.berrydex.R
import com.kkp.berrydex.data.models.BerryListEntry
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@Composable
fun BerryListScreen(
    navController: NavController?,
//    viewModel: BerryListViewModel = hiltViewModel()
) {
    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xff47cba3),
            Color(0xffb5ffff),
            Color(0xff47cba3)
        )
    )

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .background(gradient))  {
        Column(
            modifier = Modifier.background(gradient)
        ) {
            Spacer(modifier = Modifier
                .height(18.dp)
                .fillMaxWidth())
            Image(
                painter = painterResource(id = R.drawable.new_logo),
                contentDescription = "Pokemon Berries",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(48.dp))
            BerryList(navController = navController!!)
        }

        //viewModel.loadBerriesPaginated()

    }


}

@Composable
fun BerryList(
    navController: NavController,
    viewModel: BerryListViewModel = hiltViewModel()
) {
    val berryList by remember {viewModel.berryList}
    val endReached by remember {viewModel.endReached}
    val loadError by remember {viewModel.loadError}
    val isLoading by remember {viewModel.isLoading}
    val isSearching by remember { viewModel.isSearching}

    LazyColumn(contentPadding = PaddingValues(16.dp),
    ){
        val itemCount = if(berryList.size % 2 ==0){
            berryList.size / 2
        }else{
            berryList.size / 2 + 1
        }
        items(itemCount){
            if (it >= itemCount - 1 && !endReached){
                viewModel.loadBerriesPaginated()
            }
            BerryRow(
                rowIndex = it,
                entries = berryList,
                navController = navController)
        }
    }
}

@Composable
fun BerryRow(
    rowIndex : Int,
    entries : List<BerryListEntry>,
    navController: NavController
) {
    Column{
        Row{
            BerryEntry(entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2){
                BerryEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

@Composable
fun BerryEntry(
    entry : BerryListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: BerryListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember{
        mutableStateOf(defaultDominantColor)
    }
    Box(modifier = modifier
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .aspectRatio(1f)
        .background(
            Brush.verticalGradient(
                listOf(
                    dominantColor,
                    defaultDominantColor
                )
            )
        )
        .clickable {
            var berryId = entry.imageUrl.takeLast(6).dropLast(4)
            if (!berryId[0].isDigit()){
               berryId = berryId.takeLast(1)}
            navController.navigate(
                "berry_detail_screen/${dominantColor.toArgb()}/${entry.name}/${berryId}"
            )
        }
    ){

        Column(
            horizontalAlignment = CenterHorizontally) {

            Spacer(
                modifier = Modifier.fillMaxWidth().height(30.dp)
            )
            SubcomposeAsyncImage(
                model = entry.imageUrl,
                contentDescription = entry.name,
                alignment = Alignment.TopCenter
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading){
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                }
                else{
                    SubcomposeAsyncImageContent(modifier = Modifier.size(60.dp))
                    LaunchedEffect(key1 = painter){
                        launch {
                            val image = painter.imageLoader.execute(painter.request).drawable
                            viewModel.calcDominantColor(image!!){
                                dominantColor = it
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.fillMaxWidth().height(40.dp)
            )

            Text(text = entry.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}


@Preview
@Composable
fun ListPreview() {
    BerryListScreen(navController = null)
}