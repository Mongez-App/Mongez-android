package com.iti.mongez.presentation.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.util.Locale

data class FileInfo(
    val name: String,
    val sizeFormatted: String,
    val extension: String,
    val mimeType: String,
    val sizeBytes: Long
)

fun Uri.getFileInfo(context: Context): FileInfo {
    var name = "Unknown File"
    var sizeBytes = 0L

    if (this.scheme == "content") {
        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (nameIndex != -1) name = cursor.getString(nameIndex)
                if (sizeIndex != -1) sizeBytes = cursor.getLong(sizeIndex)
            }
        }
    } else if (this.scheme == "file") {
        name = this.lastPathSegment ?: name
    }

    val extension = name.substringAfterLast('.', "").uppercase(Locale.getDefault())
    val mimeType = context.contentResolver.getType(this) ?: "application/octet-stream"

    val kb = sizeBytes / 1024.0
    val mb = kb / 1024.0
    val sizeFormatted = when {
        mb >= 1 -> String.format(Locale.getDefault(), "%.1f MB", mb)
        kb >= 1 -> String.format(Locale.getDefault(), "%.0f KB", kb)
        else -> "$sizeBytes B"
    }

    return FileInfo(name, sizeFormatted, extension, mimeType, sizeBytes)
}

fun Uri.readBytes(context: Context): ByteArray? {
    return try {
        context.contentResolver.openInputStream(this)?.use { it.readBytes() }
    } catch (e: Exception) {
        null
    }
}