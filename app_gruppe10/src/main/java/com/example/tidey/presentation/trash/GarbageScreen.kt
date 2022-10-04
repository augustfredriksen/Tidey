package com.example.tidey.presentation.trash

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tidey.R
import com.example.tidey.presentation.components.BottomBar
import com.example.tidey.presentation.map.MapEvent
import com.example.tidey.presentation.map.MapViewModel
import com.example.tidey.presentation.map.components.PermissionHandler
import com.example.tidey.presentation.trash.components.GarbageButton
import com.example.tidey.presentation.map_details.components.MapDetailsTopBar
import com.example.tidey.presentation.map_details.components.TideText
import com.example.tidey.presentation.navigation.Screen
import com.example.tidey.presentation.trash.components.InfoRow
import com.example.tidey.presentation.ui.theme.green
import com.example.tidey.presentation.ui.theme.red
import com.example.tidey.presentation.ui.theme.yellow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GarbageScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel(),
) {
    var scaffoldState = rememberScaffoldState()
    var coroutineScope = rememberCoroutineScope()
    var selected by remember { mutableStateOf(viewModel.marker.value?.garbageLevel) }
    val onSelected = { text: String -> selected = text}

    val options = listOf(
        "Ryddig!",
        "Litt søppel!",
        "Masse søppel!"
    )
    PermissionHandler()

    BackHandler {
        viewModel.notReady()
        navController.navigateUp()

    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MapDetailsTopBar(
                        stringResource(R.string.Garbage_info),
                        viewModel,
                        navController
                    )
            },
            bottomBar = { BottomBar(navController = navController) },
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (viewModel.marker.value != null) {
                        Card(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                            elevation = 10.dp,
                            shape = RoundedCornerShape(15.dp)
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                InfoRow(key = "Observert", value = viewModel.marker.value!!.garbageLevel!!)
                                InfoRow(key = "Dato", value = viewModel.marker.value!!.dateReported!!)
                                InfoRow(key = "Bruker", value = viewModel.marker.value!!.user!!)
                                InfoRow(key = "Kommune", value = viewModel.country.value.toString())
                                if (viewModel.country.value.toString() !=
                                    viewModel.address.value.toString().replace(", Norge", "")) {
                                    InfoRow(key = "Sted", value = viewModel.address.value.toString().replace(", Norge", ""))
                                }


                            }
                        }
                    }
                    TideText(text = stringResource(R.string.Choose_garbage_level))
                        options.forEach { text ->
                            Row(
                                modifier = Modifier
                                    .padding(
                                        all = 8.dp,
                                    ),
                            ) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.h6,
                                    color = if (text == selected) {
                                        androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                                        }
                                     else {
                                        androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                                    },
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .width(225.dp)
                                        .clip(
                                            shape = RoundedCornerShape(
                                                size = 20.dp,
                                            ),
                                        )
                                        .clickable {
                                            onSelected(text)
                                            viewModel.garbageLevel.value = selected
                                        }
                                        .background(
                                            if (text == selected) {
                                                when (text) {
                                                    "Ryddig!" -> green
                                                    "Litt søppel!" -> yellow
                                                    "Masse søppel!" -> red
                                                    else -> androidx.compose.material3.MaterialTheme.colorScheme.secondary
                                                }
                                            } else {
                                                androidx.compose.material3.MaterialTheme.colorScheme.secondary
                                            }
                                        )
                                        .padding(
                                            vertical = 15.dp,
                                            horizontal = 20.dp,
                                        ),
                                )
                            }
                        }
                    Spacer(modifier = Modifier.height(30.dp))
                    GarbageButton {
                        when (viewModel.garbageLevel.value) {
                            null -> {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Velg et søppelnivå.")
                                }
                            }

                            else -> {
                                viewModel.onEvent(MapEvent.InsertGarbageMapPoint(viewModel.latLng.value!!))
                                navController.popBackStack()
                                navController.navigate(Screen.Map.route)
                                viewModel.notReady()
                            }
                        }
                    }
                    }
                })
            }
    }

