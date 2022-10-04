package com.example.tidey.presentation.map.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tidey.R
import com.example.tidey.presentation.map.MapViewModel


@Composable
fun MapTopBar(
    text: String,
    viewModel: MapViewModel = hiltViewModel(),
    navController: NavController
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(viewModel.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(32.dp)
                                .height(32.dp)
                        )
                    }
                }
            }
        },
        actions = {
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        viewModel.signOut(navController)
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.Sign_out)
                    )
                }
            }
        }
    )
}