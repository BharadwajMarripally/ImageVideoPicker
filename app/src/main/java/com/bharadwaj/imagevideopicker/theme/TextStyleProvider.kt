package com.bharadwaj.imagevideopicker.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

interface TextStyleProvider {
    val fontSize: TextUnit
    val fontWeight: FontWeight
    val textAlign: TextAlign
    val color: Color
}