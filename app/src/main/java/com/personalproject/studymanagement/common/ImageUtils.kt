package com.personalproject.studymanagement.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Base64
import android.view.View
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    /**
     * Saves a bitmap to the cache directory, retrieves its path, and shares it.
     *
     * @param bitmap The bitmap image to save and share.
     * @param context The context of the application.
     */
    fun shareBitmapFromCache(bitmap: Bitmap, context: Context) {
        try {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "temp_image.png")

            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider", // Replace with your FileProvider authority
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "Share Image"))
        } catch (e: Exception) {
            // Handle exceptions appropriately (e.g., log the error, show a message to the user)
            e.printStackTrace()
        }
    }

    /**
     * Converts a view to a bitmap with added padding.
     *
     * @param view The view to convert to a bitmap.
     * @param padding The padding to add around the view content (in pixels).
     * @return The bitmap representation of the view with padding, or null if an error occurs.
     */
    fun getViewBitmapWithPadding(view: View, padding: Int): Bitmap? {
        return try {
            val bitmap = Bitmap.createBitmap(
                view.width + 2 * padding,
                view.height + 2 * padding,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.translate(padding.toFloat(), padding.toFloat()) // Add padding to the canvas
            view.draw(canvas)
            bitmap
        } catch (e: Exception) {
            // Handle exceptions (e.g., log the error)
            e.printStackTrace()
            null // Return null in case of an error
        }
    }

    /**
     * Converts a bitmap image to a Base64 encoded string.
     *
     * @param bitmap The bitmap image to convert.
     * @param format The compression format (e.g., Bitmap.CompressFormat.PNG).
     * @param quality The image quality (0-100).
     * @return The Base64 encoded string representation of the bitmap, or null if an error occurs.
     */
    fun bitmapToBase64(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): String? {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            // Handle the exception (e.g., log the error)
            e.printStackTrace()
            null // Return null in case of an error
        }
    }

    /**
     * Converts a Base64 encoded string to a bitmap image.
     *
     * @param base64String The Base64 encoded string representing the image.
     * @return The decoded bitmap image, or null if an error occurs.
     */
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}