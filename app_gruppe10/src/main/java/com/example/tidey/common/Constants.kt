package com.example.tidey.common

import androidx.compose.ui.platform.LocalContext
import com.example.tidey.R
import com.example.tidey.presentation.navigation.Screen
import com.google.android.gms.maps.model.LatLng

object Constants {
    //Names
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    //Screens
    const val SPLASH_SCREEN = "Splash"
    const val AUTH_SCREEN = "Authentication"
    const val MAP_SCREEN = "Map"
    const val MAP_DETAIL_SCREEN = "Overview"
    const val STATISTIC_SCREEN = "Statistic"
    const val GARBAGE_SCREEN = "Garbage"

    //Base url API
    const val BASE_URL = "http://api.sehavniva.no"

    //Location
    val NORGE = LatLng(64.3925 , 10.421906)
}