package com.iti.mongez.presentation.coursedetails.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import androidx.core.net.toUri

object PdfOpener {

    suspend fun openPdf(
        context: Context,
        uriString: String,
        onError: suspend (String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val rawUri = uriString.toUri()

                // 1. Convert restricted URIs (like content:// or file://) into safe FileProvider URIs
                val uriToShare: Uri = when (rawUri.scheme) {
                    "content" -> {
                        // Copy the Storage Access Framework content to a temporary cache file
                        val inputStream = context.contentResolver.openInputStream(rawUri)
                        val tempFile = File(context.cacheDir, "temp_document.pdf")
                        val outputStream = FileOutputStream(tempFile)
                        inputStream?.copyTo(outputStream)
                        inputStream?.close()
                        outputStream.close()

                        // Generate a secure FileProvider URI (using the provider from your Manifest)
                        androidx.core.content.FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            tempFile
                        )
                    }
                    "file" -> {
                        val file = File(rawUri.path ?: "")
                        androidx.core.content.FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                    }
                    else -> rawUri // Let http/https URLs pass through normally
                }

                // 2. Launch the Intent on the Main Thread
                withContext(Dispatchers.Main) {
                    val pdfIntent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uriToShare, "application/pdf")
                        // This flag is critical for allowing Adobe to read the FileProvider URI
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(pdfIntent)
                }

            } catch (e: android.content.ActivityNotFoundException) {
                // ONLY show this message if the system truly cannot find a PDF viewer
                onError("No PDF viewer app found to open this document")
            } catch (e: Exception) {
                // Display the actual error for debugging if it's a security/IO issue
                onError("Failed to load document: ${e.localizedMessage}")
            }
        }
    }
}
