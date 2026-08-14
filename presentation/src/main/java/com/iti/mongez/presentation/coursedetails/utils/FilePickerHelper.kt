package com.iti.mongez.presentation.coursedetails.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FilePickerHelper {

    @Composable
    fun rememberFilePickerLauncher(
        context: Context,
        coroutineScope: CoroutineScope,
        onFileSelected: (fileName: String, mimeType: String, fileSize: Long, bytes: ByteArray, uriString: String) -> Unit
    ): ManagedActivityResultLauncher<Array<String>, Uri?> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let { selectedUri ->
                // Tell Android to permanently keep read access to this URI
                try {
                    context.contentResolver.takePersistableUriPermission(
                        selectedUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace() // Fallback if the specific provider doesn't allow persistable permissions
                }

                coroutineScope.launch(Dispatchers.IO) {
                    val contentResolver = context.contentResolver
                    val mimeType = contentResolver.getType(selectedUri) ?: "application/pdf"

                    var fileName = "document.pdf"
                    var fileSize = 0L

                    contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (cursor.moveToFirst()) {
                            fileName = cursor.getString(nameIndex)
                            fileSize = cursor.getLong(sizeIndex)
                        }
                    }

                    val inputStream = contentResolver.openInputStream(selectedUri)
                    val bytes = inputStream?.readBytes() ?: ByteArray(0)
                    inputStream?.close()

                    if (bytes.isNotEmpty()) {
                        onFileSelected(fileName, mimeType, fileSize, bytes, selectedUri.toString())
                    }
                }
            }
        }
    }
}
