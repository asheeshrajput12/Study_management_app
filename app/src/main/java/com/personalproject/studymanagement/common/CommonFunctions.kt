package com.personalproject.studymanagement.common

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.CallLog
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CommonFunctions {

    fun getCommonCursorData():Cursor?{
        try {

        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }
    fun insertData(context: Context, strTable:String, value:ContentValues){
        try {
            // Construct the URI for your content provider table
            val uri = Uri.parse("content://com.personalproject.studymanagement.provider/$strTable")
            val resolver=MyContentProvider()
            val resultUri=resolver.insert(uri,value)
            if (resultUri != null) {
                println("Data inserted successfully: $resultUri")
            } else {
                println("Failed to insert data.")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

   const val READ_CALL_LOG_PERMISSION_REQUEST = 123

    fun saveCallLogsToFile(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                READ_CALL_LOG_PERMISSION_REQUEST
            )
        } else {
            // Permission already granted, proceed with saving call logs
            getCallLogsAndSave(context)
        }
    }
     fun getCallLogsAndSave(context: Context) {
        val uri = CallLog.Calls.CONTENT_URI
         val selection = CallLog.Calls.DATE + " BETWEEN ? AND ?"
         val selectionArgs = arrayOf(getLastMidnightTimestamp().toString(), getTodayMidnightTimestamp().toString())
        val sortOrder = CallLog.Calls.DATE + " DESC"

        val cursor = context.contentResolver.query(uri, null, selection, selectionArgs, sortOrder)
        val callLogs = StringBuilder()

        if (cursor != null && cursor.moveToFirst()) {
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)

            do {
                val phoneNumber = cursor.getString(numberIndex)
                val callType = when (cursor.getInt(typeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> "MISSED"
                    else -> "UNKNOWN"
                }
                val callDate = Date(cursor.getLong(dateIndex))
                val callDuration = cursor.getString(durationIndex)

                val logMessage = """
            |Phone Number: $phoneNumber
            |Call Type: $callType
            |Call Date: ${
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(callDate)
                }
            |Call duration in sec: $callDuration
            |--------------------
        """.trimMargin()

                callLogs.append(logMessage).append("\n")
                Timber.d(logMessage)
            } while (cursor.moveToNext())

            cursor.close()
        }

       /* try {
            val file = File(Environment.getExternalStorageDirectory(), "CallLogs.txt")
            val writer = FileWriter(file)
            writer.append(callLogs.toString())
            writer.flush()
            writer.close()
            Log.d("CallLogs", "Call logs saved to ${file.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("CallLogs", "Error saving call logs to file", e)
        }*/
    }

    private fun getTodayMidnightTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    private fun getLastMidnightTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DATE, -1) // Subtract one day
        return calendar.timeInMillis
    }
    fun addMandatoryAsterisk(textView: TextView) {
        val labelText = textView.text.toString()
        val mandatoryText = "$labelText <font color='#FF0000'>*</font>"

        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(mandatoryText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(mandatoryText)
        }
    }


}