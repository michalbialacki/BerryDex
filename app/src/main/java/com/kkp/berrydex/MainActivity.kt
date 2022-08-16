package com.kkp.berrydex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kkp.berrydex.berrylist.BerryListScreen
import com.kkp.berrydex.ui.theme.BerryDexTheme
import dagger.hilt.android.AndroidEntryPoint

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
                        "Berry_Detail_Screen/{dominantColor}/{berryName}",
                                arguments = listOf(
                                    navArgument("dominantColor"){
                                        type = NavType.IntType
                                    },
                                    navArgument("berryName"){
                                        type = NavType.StringType
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
                        TODO("Add detail screen & navigation for it")
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