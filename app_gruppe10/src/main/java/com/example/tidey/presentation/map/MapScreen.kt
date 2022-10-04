package com.example.tidey.presentation.map

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tidey.R
import com.example.tidey.common.Constants.NORGE
import com.example.tidey.domain.model.Response.*
import com.example.tidey.presentation.components.ProgressBar
import com.example.tidey.presentation.map.components.MapTopBar
import com.example.tidey.presentation.navigation.Screen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.*
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MissingPermission")
@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    viewModel.addTestHashMap()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())
    var tideState = viewModel.tide.observeAsState(viewModel.tide.value?.locationdata?.data)
    var currentLocation: LatLng = NORGE
    var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    viewModel.addMapPointsToRoom()

    LaunchedEffect(key1 = tideState.value) {
        if (viewModel.tide.value?.locationdata?.data != null) {
            navController.navigate(Screen.MapDetail.route)
        }
        if (viewModel.tide.value?.locationdata?.noData != null) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.No_data_found))
                viewModel.notReady()
            }
        }
    }

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = if (viewModel.latLng.value == null) {
            CameraPosition.fromLatLngZoom(currentLocation, 4f)
        } else {
            viewModel.latLng.value?.let { CameraPosition.fromLatLngZoom(it, 10f) }!!
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MapTopBar(viewModel.displayName, viewModel, navController) },
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                viewModel.onEvent(MapEvent.ToggleDarkMode)
            },
            backgroundColor = MaterialTheme.colorScheme.primary) {
                Icon(
                    imageVector = if (viewModel.state.isDarkMap) {
                    Icons.Default.ToggleOff
                } else Icons.Default.ToggleOn,
                    contentDescription = "Toggle dark map",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            GoogleMap(
                uiSettings = uiSettings,
                cameraPositionState = cameraPositionState,
                modifier = Modifier.matchParentSize(),
                properties = viewModel.state.properties,
                onMapClick = {
                    viewModel.onEvent(MapEvent.OnMapClick(it))
                }
            ) {
                if (viewModel.state.garbageMapPoint.isNotEmpty()) {
                    viewModel.state.garbageMapPoint.forEach { spot ->

                        var color: Float = 0f
                        var position = LatLng(spot.lat!!, spot.lng!!)

                        viewModel.getInfoFromMarker(geocoder, position.latitude, position.longitude)
                        when (spot.garbageLevel) {
                            "Ryddig!" -> color = BitmapDescriptorFactory.HUE_GREEN
                            "Litt søppel!" -> color = BitmapDescriptorFactory.HUE_YELLOW
                            "Masse søppel!" -> color = BitmapDescriptorFactory.HUE_RED
                        }
                        Marker(
                            position = LatLng(position.latitude, position.longitude),
                            title = spot.garbageLevel,
                            snippet = "${context.getString(R.string.Last_observed)} ${spot.dateReported} ${viewModel.country.value}",
                            onInfoWindowClick = {
                                it.hideInfoWindow()
                                viewModel.onEvent(
                                    MapEvent.OnMapClick(
                                        LatLng(
                                            spot.lat!!,
                                            spot.lng!!
                                        )
                                    )
                                )
                                viewModel.onEvent(MapEvent.OnInfoWindowClick(spot))
                            },
                            onInfoWindowLongClick = {
                                viewModel.onEvent(MapEvent.OnInfoWindowLongClick(spot))
                                it.hideInfoWindow()
                            },
                            onClick = {
                                it.showInfoWindow()
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(color)
                        )
                    }
                }
            }
        }




        when (val signOutResponse = viewModel.signOutState.value) {
            is Loading -> ProgressBar()
            is Success<*> -> {
                val signedOut = signOutResponse.data
                signedOut?.let {
                    if (signedOut as Boolean) {
                    }
                }
            }
            is Failure -> signOutResponse.e?.let {
                LaunchedEffect(Unit) {
                    print(it)
                }
            }
        }
    }
}




