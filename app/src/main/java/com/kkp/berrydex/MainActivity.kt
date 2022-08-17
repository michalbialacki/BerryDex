package com.kkp.berrydex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kkp.berrydex.BerryList.BerryListScreen
import com.kkp.berrydex.BerryList.BerryListViewModel
import com.kkp.berrydex.berrydetail.BerryDetailScreen
import com.kkp.berrydex.ui.theme.BerryDexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BerryDexTheme {
            val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Berry_List_Screen"){
                    composable("Berry_List_Screen"){
                        BerryListScreen(navController = navController)
                    }
                    composable(
                        "Berry_Detail_Screen/{dominantColor}/{berryName}/{berryId}",
                                arguments = listOf(
                                    navArgument("dominantColor"){
                                        type = NavType.IntType
                                    },
                                    navArgument("berryName"){
                                        type = NavType.StringType
                                    },
                                    navArgument("berryId"){
                                        type = NavType.IntType
                                    }
                                )
                    ){
                        val dominantColor = remember{
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) ?: Color.White }
                        }
                        val berryName = remember{
                            it.arguments?.getString("berryName")
                        }
                        val berryId = remember{
                            it.arguments?.getInt("berryId")
                        }
                        BerryDetailScreen(
                            dominantColor = dominantColor ?: Color.White,
                            berryName = berryName?.toLowerCase(Locale.ROOT) ?: "",
                            berryId = berryId ?: 0,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BerryDexTheme {
        Greeting("Android")
    }
}