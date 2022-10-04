package com.example.tidey.presentation.map_details


import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center

import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tidey.R
import com.example.tidey.presentation.components.BottomBar
import com.example.tidey.presentation.map.MapEvent

import com.example.tidey.presentation.map.MapViewModel
import com.example.tidey.presentation.map.components.MapTopBar
import com.example.tidey.presentation.map_details.components.*
import com.example.tidey.presentation.theme.*
import java.util.*


@Composable
fun MapDetailScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    Log.d("Test7", "MapDetailScreen: ${viewModel.tide.value?.locationdata.toString()}")
    var scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())
    Log.d("Test9", "tide value inside detail screen: ${viewModel.tide.value}")
    viewModel.addTestHashMap()
    if (viewModel.marker.value != null) {
        viewModel.getInfoFromMarker(
            geocoder,
            viewModel.marker.value!!.lat!!,
            viewModel.marker.value!!.lng!!
        )
    }

    BackHandler {
        viewModel.notReady()
        navController.navigateUp()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MapDetailsTopBar(
                    stringResource(R.string.Overview),
                    viewModel,
                    navController
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            backgroundColor = MaterialTheme.colorScheme.background,
            content = { padding ->
                Box(modifier = Modifier.fillMaxSize())
                {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {


                        Spacer(modifier = Modifier.height(20.dp))
                        var garbageText = ""
                        when (viewModel.marker.value?.garbageLevel) {
                            "Ryddig!" -> garbageText = "Det er ryddig i ${
                                viewModel.address.value?.replace(
                                    ", Norge",
                                    ""
                                )
                            }. Fortsett sånn!"
                            "Litt søppel!" -> garbageText = "Det er observert litt søppel i ${
                                viewModel.address.value?.replace(
                                    ", Norge",
                                    ""
                                )
                            }. Ta deg en spasertur og plukk litt søppel!"
                            "Masse søppel!" -> garbageText = "Det er observert masse søppel i ${
                                viewModel.address.value?.replace(
                                    ", Norge",
                                    ""
                                )
                            }. Her må det ryddes ASAP!"
                        }

                        if (garbageText.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(32.dp),
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Detail icon",
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                                )
                                Text(
                                    text = garbageText,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                imageVector = Icons.Default.Info,
                                contentDescription = "Detail icon",
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                            )
                            Text(
                                text = viewModel.tideText.value.toString(),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ic_beach_image_dark),
                            contentDescription = "Beach cleaning image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1.3f)
                                .padding(bottom = 45.dp)
                                .fillMaxSize(),
                        )
                    }
                }
            }
        )
    }
}

