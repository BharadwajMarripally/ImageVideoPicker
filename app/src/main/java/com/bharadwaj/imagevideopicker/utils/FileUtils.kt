package com.bharadwaj.imagevideopicker.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.bharadwaj.imagevideopicker.constants.Keys
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val destination = context.cacheDir.path
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(destination)
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFile(path: String): File? {
        return File(path)
    }

    fun getFileExtension(context: Context, uri: Uri): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            // If it's a content URI, resolve the MIME type and map it to an extension
            val mimeType = context.contentResolver.getType(uri)
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        } else {
            // If it's a file URI, extract the extension directly
            val file = File(uri.path ?: "")
            file.extension.ifEmpty { null }
        }
    }

    fun getFileTypeFromUrl(url: String): String {
        val extension = url.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg" -> Keys.Image
            "mp4", "avi", "mov", "mkv", "flv", "wmv", "webm" -> Keys.Video
            "mp3", "wav", "ogg", "flac", "aac" -> Keys.Audio
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt" -> Keys.Document
            "zip", "rar", "7z", "tar", "gz" -> Keys.Archive
            else -> Keys.Unknown
        }
    }
}