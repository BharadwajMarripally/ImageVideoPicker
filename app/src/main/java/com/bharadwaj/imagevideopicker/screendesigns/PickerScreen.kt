package com.bharadwaj.imagevideopicker.screendesigns

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bharadwaj.imagevideopicker.PickerActivity
import com.bharadwaj.imagevideopicker.R
import com.bharadwaj.imagevideopicker.constants.Keys
import com.bharadwaj.imagevideopicker.pojo.GalleryModel
import com.bharadwaj.imagevideopicker.ui.theme.BoldBlackTextStyle24SPCenter
import com.bharadwaj.imagevideopicker.ui.theme.NormalBlackTextStyle15SPCenter
import com.bharadwaj.imagevideopicker.utils.FileUtils
import com.bharadwaj.imagevideopicker.utils.Globals
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTypePicker(activity: PickerActivity) {

    var cameraClick: Unit
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Load the captured image
                imageUri?.let { uri ->
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        capturedImage = bitmap.asImageBitmap() // Convert to ImageBitmap
                        val galleryModel = GalleryModel()
                        galleryModel.filePath = imageUri?.path?:""
                        galleryModel.itemUrI = imageUri
                        galleryModel.bitmap = bitmap
                        val galleryStringObj = Gson().toJson(galleryModel)
                        Globals.savePreference(activity, Keys.GALLERY_OBJECT, galleryStringObj)
                        activity.setResult(Activity.RESULT_OK, Intent().putExtra(GalleryModel::class.java.name, galleryStringObj))
                        activity.finish()
                    }

                }



            }
        }
    val createImageFile = {
        val storageDir = context.cacheDir
        val file = File.createTempFile("captured_image", ".jpg", storageDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        file to uri
    }

    ///////////////////////////////////////////////////////////////////////////////


    var galleryImageLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedUri = uri

            if (selectedUri != null) {
                val fileExtension = FileUtils.getFileExtension(context, selectedUri!!)
                val destinationPath = context.filesDir.path + "/my_saved_file.$fileExtension"
                val savedFile = FileUtils.saveFileFromUri(context, selectedUri!!, destinationPath)
                var filePath = savedFile?.absolutePath ?: ""
                imageUri = Uri.fromFile(savedFile)

                val galleryModel = GalleryModel()
                galleryModel.filePath = imageUri?.path?:""
                galleryModel.itemUrI = imageUri
                val galleryStringObj = Gson().toJson(galleryModel)
                Globals.savePreference(activity, Keys.GALLERY_OBJECT, galleryStringObj)

                activity.setResult(Activity.RESULT_OK, Intent().putExtra(GalleryModel::class.java.name, galleryStringObj))
                activity.finish()

            }

        }

    ///////////////////////////////////////////////////////////////////////////////

    // State for managing the bottom sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Ensures it only opens fully
    )
    val coroutineScope = rememberCoroutineScope()

    // State to control whether the sheet is visible
    var isSheetOpen by remember { mutableStateOf(true) }
    if (capturedImage != null) {
        Image(
            bitmap = capturedImage!!,
            contentDescription = "Captured Image",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )
    }
    // ModalBottomSheet composable
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide() // Animate closing the bottom sheet
                }.invokeOnCompletion {
                    isSheetOpen = false
                }
            },
            sheetState = sheetState
        ) {
            // Bottom sheet content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledText("Choose an option", styleProvider = BoldBlackTextStyle24SPCenter)
                Spacer(modifier = Modifier.height(28.dp))

                StyledText(
                    "Camera", styleProvider = NormalBlackTextStyle15SPCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                        .clickable(
                            onClick = {
                                val (file, uri) = createImageFile()
                                imageUri = uri
                                cameraLauncher.launch(uri)
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = colorResource(R.color.black))
                        )
                        .padding(vertical = 15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                StyledText(
                    "Gallery", styleProvider = NormalBlackTextStyle15SPCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                        .clickable(
                            onClick = {
                                val intent = Intent(Intent.ACTION_GET_CONTENT)
                                intent.type = "image/*"
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                galleryImageLauncher.launch("image/*")
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = colorResource(R.color.black))
                        )
                        .padding(vertical = 15.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                /*Button(onClick = {
                    coroutineScope.launch {
                        sheetState.hide() // Close the bottom sheet with animation
                        isSheetOpen = false
                    }.invokeOnCompletion {
                        isSheetOpen = false
                        activity.finish()
                    }
                }) {
                    StyledText("Close", styleProvider = NormalBlackTextStyle15SPCenter)
                }*/
            }
        }
    }
}

@Composable
fun DialogTypePicker() {
    val context = LocalContext.current

}