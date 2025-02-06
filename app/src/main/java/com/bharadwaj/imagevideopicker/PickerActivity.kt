package com.bharadwaj.imagevideopicker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.bharadwaj.imagevideopicker.constants.Keys
import com.bharadwaj.imagevideopicker.screendesigns.BottomSheetTypePicker
import com.bharadwaj.imagevideopicker.ui.theme.ImageVideoPickerTheme
import com.bharadwaj.imagevideopicker.utils.Globals

class PickerActivity : ComponentActivity() {

    private var pickerType = ""
    private var pickerMode = ""
    private val permissions = arrayOf(
        Manifest.permission.CAMERA
    )
    private var showBottomSheet by mutableStateOf(false)
    private var showPermissionDialog by mutableStateOf(false)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.all { it.value }) {
                showBottomSheet = true
            } else {
                showPermissionDialog = true // Show dialog if permissions are denied
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickerType = intent?.getStringExtra(Keys.PICKER_TYPE) ?: PickerType.BottomSheet.name
        pickerMode = intent?.getStringExtra(Keys.PICKER_MODE) ?: PickerMode.IMAGE.name
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setDimAmount(0.5f)
        // Check and request permissions before showing bottom sheet
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            showBottomSheet = true
        } else {
            permissionLauncher.launch(permissions)
        }
        setContent {
            ImageVideoPickerTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // Ensure Compose does not apply a white background
                ) {
                    if (showBottomSheet) {
                        BottomSheetTypePicker(this@PickerActivity, pickerMode)
                    }
                    if (showPermissionDialog) {
                        PermissionDeniedDialog()
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val galleryStringObj = Globals.loadPreference(this@PickerActivity, Keys.GALLERY_OBJECT)
        Globals.log(this@PickerActivity, galleryStringObj)
    }

    companion object {
        fun launchPicker(
            context: Context,
            pickerType: String = PickerType.BottomSheet.name,
            pickerMode: String = PickerMode.IMAGE.name
        ) {
            context.startActivity(
                Intent(
                    context, PickerActivity::class.java
                )
                    .putExtra(Keys.PICKER_TYPE, pickerType)
                    .putExtra(Keys.PICKER_MODE, pickerMode)
            )
        }
    }

    @Composable
    fun PermissionDeniedDialog() {
        AlertDialog(
            onDismissRequest = {
                showPermissionDialog = false
                finish() // Close activity if dismissed
            },
            title = { Text("Permissions Required") },
            text = { Text("This feature requires Camera and Storage permissions. Please enable them in settings.") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        openAppSettings()
                        finish() // Close activity after opening settings
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        finish() // Close activity if denied
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }
}