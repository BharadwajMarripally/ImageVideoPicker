package com.bharadwaj.imagevideopicker.screendesigns

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bharadwaj.imagevideopicker.PickerActivity
import com.bharadwaj.imagevideopicker.PickerMode
import com.bharadwaj.imagevideopicker.R
import com.bharadwaj.imagevideopicker.constants.Keys
import com.bharadwaj.imagevideopicker.listener.InterfaceHolder
import com.bharadwaj.imagevideopicker.pojo.GalleryModel
import com.bharadwaj.imagevideopicker.ui.theme.BoldBlackTextStyle24SPCenter
import com.bharadwaj.imagevideopicker.ui.theme.NormalBlackTextStyle15SPCenter
import com.bharadwaj.imagevideopicker.utils.Globals
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTypePicker(activity: PickerActivity, pickerMode: String) {

    // State for managing the bottom sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Ensures it only opens fully
    )
    val coroutineScope = rememberCoroutineScope()

    // State to control whether the sheet is visible
    var isSheetOpen by remember { mutableStateOf(true) }
    // ModalBottomSheet composable
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide() // Animate closing the bottom sheet
                }.invokeOnCompletion {
                    isSheetOpen = false
                    activity.finish()
                }
            },
            sheetState = sheetState,
            containerColor = Color.White, // Keep only the bottom sheet visible
            scrimColor = Color.Transparent // Remove dimmed background
        ) {
            // Bottom sheet content
            if (pickerMode == PickerMode.IMAGE.name ||
                pickerMode == PickerMode.MULTIPLE_IMAGE.name){
                ImageBottomSheet(activity, pickerMode)
            }
            if (pickerMode == PickerMode.VIDEO.name ||
                pickerMode == PickerMode.MULTIPLE_VIDEO.name){
                VideoBottomSheet(activity, pickerMode)
            }
        }
    }
}