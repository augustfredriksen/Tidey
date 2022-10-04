package com.example.tidey.presentation.statistic

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tidey.R
import com.example.tidey.presentation.components.BottomBar
import com.example.tidey.presentation.map.MapViewModel
import com.example.tidey.presentation.map.components.MapTopBar
import com.example.tidey.presentation.map_details.components.MapDetailsTopBar
import com.example.tidey.presentation.map_details.components.TideText
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun StatisticScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel()
) {
    var scaffoldState = rememberScaffoldState()

    BackHandler {
        viewModel.notReady()
        navController.navigateUp()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MapDetailsTopBar(
                    stringResource(R.string.Tide_level),
                    viewModel,
                    navController
                )
            },
            bottomBar = { BottomBar(navController = navController) },
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TideText(text = "${LocalContext.current.getString(R.string.Tide_level)} for ${viewModel.currentDate(0, "dd.MM.yyyy")}")
                    Text(text = stringResource(R.string.x_axis), color = MaterialTheme.colorScheme.onBackground)
                    Text(text = stringResource(R.string.Y_axis), color = MaterialTheme.colorScheme.onBackground)
                    val lineList: MutableList<LineChartData.Point> = mutableListOf()
                    viewModel.tidePrediction.value?.locationdata?.data?.waterLevelList?.forEach { waterLevel ->
                        waterLevel.value?.toFloat()?.let {
                            LineChartData.Point(
                                it,
                                waterLevel.time.toString().drop(11).plus(":00")
                                    .dropLast(12)
                            )
                        }?.let { lineList.add(it) }
                    }
                    LineChart(
                        lineChartData = LineChartData(
                            points = lineList
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .height(400.dp)
                            .padding(20.dp),
                        animation = simpleChartAnimation(),
                        pointDrawer = FilledCircularPointDrawer(
                            diameter = 1.dp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                        ),
                        lineDrawer = SolidLineDrawer(
                            thickness = 3.dp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                        ),
                        xAxisDrawer = SimpleXAxisDrawer(
                            drawLabelEvery = 18,
                            axisLineThickness = 2.dp,
                            labelTextColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                            axisLineColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                        ),
                        yAxisDrawer = SimpleYAxisDrawer(
                            drawLabelEvery = 4,
                            axisLineThickness = 2.dp,
                            labelTextColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                            axisLineColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                        ),
                        horizontalOffset = 1f
                    )
                    viewModel.tide.value?.locationdata?.data?.waterLevelList?.forEach { waterLevel ->
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .border(.5.dp, androidx.compose.material3.MaterialTheme.colorScheme.secondary)
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TideText(
                                text = "kl. ${
                                    waterLevel.time.toString().drop(11)
                                        .dropLast(9)
                                }"
                            )
                            TideText(
                                text = waterLevel.flag.toString()
                                    .replace("high", stringResource(R.string.High_tide)).replace("low", stringResource(
                                                                            R.string.Low_tide)
                                                                        )
                            )
                            TideText(
                                text = "${waterLevel.value}cm"
                            )
                        }

                    }
                }

            })
    }
}