package com.rahul.stocker.ext.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rahul.stocker.R

private val GoogleSans =
    FontFamily(
        Font(R.font.google_sans_regular, FontWeight.Normal),
        Font(R.font.google_sans_medium, FontWeight.Medium),
        Font(R.font.google_sans_bold, FontWeight.Bold),
    )

val AppTypography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
            ),
    )
