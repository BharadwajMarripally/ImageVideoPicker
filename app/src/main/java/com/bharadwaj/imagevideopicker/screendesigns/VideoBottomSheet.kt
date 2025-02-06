package com.bharadwaj.imagevideopicker.screendesigns

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.io.File

@Composable
fun VideoBottomSheet(activity: PickerActivity, pickerMode: String) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val clipData = data.clipData
                val galleryList = ArrayList<GalleryModel>()
                val uriList = mutableListOf<Uri>()

                if (clipData != null) {
                    // Multiple images selected
                    for (i in 0 until clipData.itemCount) {
                        imageUri = clipData.getItemAt(i).uri
                        val galleryModel = GalleryModel()
                        galleryModel.filePath = imageUri?.path?:""
                        galleryModel.itemUrI = imageUri
                        val galleryStringObj = Gson().toJson(galleryModel)
                        Globals.savePreference(activity, Keys.GALLERY_OBJECT, galleryStringObj)
                        galleryList.add(galleryModel)
                        uriList.add(clipData.getItemAt(i).uri)
                    }
                } else {
                    // Single image selected
                    data.data?.let {
                        val galleryModel = GalleryModel()
                        galleryModel.filePath = imageUri?.path?:""
                        galleryModel.itemUrI = imageUri
                        val galleryStringObj = Gson().toJson(galleryModel)
                        Globals.savePreference(activity, Keys.GALLERY_OBJECT, galleryStringObj)
                        galleryList.add(galleryModel)
                        uriList.add(it)
                    }
                }
                // Handle the list of selected images (uriList)
                val myInterface = InterfaceHolder.onResultCallback
                myInterface?.onResult(galleryList)
                activity.setResult(Activity.RESULT_OK, Intent()/*.putExtra(GalleryModel::class.java.name, galleryStringObj)*/)
                activity.finish()
            }
        }
    }

    // Function to create a video file and return its Uri
    val createVideoFile = {
        val storageDir = context.cacheDir
        val file = File.createTempFile("recorded_video", ".mp4", storageDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        file to uri
    }

// Launcher for capturing a video
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { isSuccess ->
        if (isSuccess) {
            // Handle the recorded video URI (videoUri)
            Log.d("VideoCapture", "Video recorded successfully: $videoUri")
            val galleryModel = GalleryModel()
            galleryModel.filePath = imageUri?.path?:""
            galleryModel.itemUrI = imageUri
            val galleryStringObj = Gson().toJson(galleryModel)
            val galleryList = ArrayList<GalleryModel>()
            galleryList.add(galleryModel)
            Globals.savePreference(activity, Keys.GALLERY_OBJECT, galleryStringObj)
            val myInterface = InterfaceHolder.onResultCallback
            myInterface?.onResult(galleryList)
            activity.setResult(Activity.RESULT_OK, Intent().putExtra(GalleryModel::class.java.name, galleryStringObj))
            activity.finish()
        } else {
            Log.e("VideoCapture", "Video recording failed")
        }
    }

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
                        val (file, uri) = createVideoFile()
                        imageUri = uri
                        videoLauncher.launch(uri)
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
                        val isMultiSelect = pickerMode == PickerMode.MULTIPLE_VIDEO.name
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            type = "video/*"
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiSelect)
                            addCategory(Intent.CATEGORY_OPENABLE)
                            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/mp4", "video/mkv", "video/webm", "video/3gp", "video/avi"))
                        }


                        galleryLauncher.launch(intent)
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = colorResource(R.color.black))
                )
                .padding(vertical = 15.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}