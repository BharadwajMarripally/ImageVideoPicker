package com.bharadwaj.imagevideopicker.screendesigns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bharadwaj.imagevideopicker.R
import com.bharadwaj.imagevideopicker.ui.theme.TextStyleProvider

@Composable
fun TextComponent(
    text: String = "Text",
    fontFamily: FontFamily = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal)
    ),
    fontSize: Float = 12f,
    color: Color = Color.Black,
    backgroundColor: Color = Color.Transparent,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    click: () -> Unit = {},
    textAlignment: TextAlign = TextAlign.Start,
    isClickable: Boolean = true
) {
    Text(
        text = text,
        fontFamily = fontFamily,
        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
        fontSize = fontSize.sp,
        color = color,
        textAlign = textAlignment,
        modifier = if(isClickable)modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(padding)
            .clickable(onClick = click) else modifier,


        )
}

@Composable
fun StyledText(
    text: String,
    styleProvider: TextStyleProvider,
    modifier: Modifier = Modifier
) {
    BasicText(
        text = text,
        style = TextStyle(
            fontSize = styleProvider.fontSize,
            fontWeight = styleProvider.fontWeight,
            textAlign = styleProvider.textAlign,
            color = styleProvider.color,
            fontFamily = FontFamily(
                Font(R.font.poppins_regular)
            )
        ),
        modifier = modifier
    )
}