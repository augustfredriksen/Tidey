package com.example.tidey.presentation.auth.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tidey.R

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        ),
        border = BorderStroke(0.5.dp, androidx.compose.material3.MaterialTheme.colorScheme.onPrimary)
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_google_logo
            ),
            contentDescription = "Google Logo"
        )
        Text(
            text = LocalContext.current.getString(R.string.Sign_in_with_google),
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}