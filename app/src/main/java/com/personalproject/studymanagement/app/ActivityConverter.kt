package com.personalproject.studymanagement.app

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.personalproject.studymanagement.databinding.LayoutActivityConverterBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ActivityConverter : AppCompatActivity() {

    private lateinit var binding: LayoutActivityConverterBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the image selection button click event
        binding.btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        // Handle the convert button click event
        binding.btnConvert.setOnClickListener {
            if (selectedImageUri != null) {
                // Start the conversion process
                convertImageToPdf(selectedImageUri!!)
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to open the image picker
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    // Activity result launcher for getting the selected image
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            binding.tvSelectedFile.text = "Selected Image: ${selectedImageUri?.path}"
        }
    }

    // Convert the selected image to PDF
    private fun convertImageToPdf(imageUri: Uri) {
        try {
            // Get the bitmap from the image URI
            val bitmap = getBitmapFromUri(imageUri)

            if (bitmap != null) {
                // Create a PDF document
                val pdfDocument = PdfDocument()

                // Create PageInfo for the PDF page
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()

                // Start a page
                val page = pdfDocument.startPage(pageInfo)

                // Draw the bitmap onto the page's canvas
                val canvas = page.canvas
                canvas.drawBitmap(bitmap, 0f, 0f, null)

                // Finish the page
                pdfDocument.finishPage(page)

                // Define the output PDF file
                val outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val outputFile = File(outputPath, "converted_image.pdf")

                // Write the PDF content to the file
                pdfDocument.writeTo(FileOutputStream(outputFile))

                // Close the PDF document
                pdfDocument.close()

                Toast.makeText(this, "PDF saved to: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error during conversion: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper method to get a Bitmap from the Uri
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val contentResolver: ContentResolver = contentResolver
        var inputStream: InputStream? = null
        return try {
            inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } finally {
            inputStream?.close()
        }
    }
}
