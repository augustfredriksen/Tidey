package com.example.tidey.presentation.map.components

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tidey.R
import com.example.tidey.common.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler() {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        permissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.CAMERA -> {
                    when {
                        perm.hasPermission -> {
                            Text(text = stringResource(R.string.Camera_permission_accepted))
                        }
                        perm.shouldShowRationale -> {
                            Text(
                                text = stringResource(R.string.Camera_permission_needed)
                            )
                        }
                        perm.isPermanentlyDenied() -> {
                            Text(
                                text = stringResource(R.string.Camera_permission_permanently_denied)
                            )
                        }
                    }
                }
            }
        }

    }
}