package com.kkp.berrydex.berrylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kkp.berrydex.R

@Composable
fun BerryListScreen(
    navController: NavController?
) {
    Surface (
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize())  {
        Column {
            Spacer(modifier = Modifier
                .height(18.dp)
                .fillMaxWidth())
            Image(
                painter = painterResource(id = R.drawable.berries_logo),
                contentDescription = "Pokemon Berries",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
        }

    }


}

@Composable
fun BerryList(
    navController: NavController,
    viewModel: BerryListViewModel = hiltViewModel()
) {

}

@Preview
@Composable
fun ListPreview() {
    BerryListScreen(navController = null)
}