package com.kkp.berrydex.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kkp.berrydex.R


val Merriweather = FontFamily(
    Font(R.font.merriweather_regular),
    Font(R.font.merriweather_bold,FontWeight.Bold),
    Font(R.font.merriweather_light, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Justify,
        color = Color.Black,
        fontFamily = Merriweather,
        fontSize = 12.sp
    ),
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontFamily = Merriweather,
        fontSize = 20.sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontFamily = Merriweather,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontFamily = Merriweather,
        fontSize = 18.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)