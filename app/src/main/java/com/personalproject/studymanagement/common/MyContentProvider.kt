package com.personalproject.studymanagement.common


import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class MyContentProvider(val mContext: Context?=null) : ContentProvider() {

    companion object {
        private const val MY_TABLE = 1
        private const val MY_TABLE_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("content://com.studymanagement.provider/","tbl_project",1)
        }
        private lateinit var dbHelper: MyDatabaseHelper
    }

    override fun onCreate(): Boolean {
        dbHelper = MyDatabaseHelper(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            MY_TABLE -> db.query(DatabaseContract.MyTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            MY_TABLE_ID -> {
                val id = ContentUris.parseId(uri)
                db.query(DatabaseContract.MyTable.TABLE_NAME, projection, "${DatabaseContract.MyTable}=?", arrayOf(id.toString()), null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            MY_TABLE -> "content://com.studymanagement.provider/tbl_project"
          //  MY_TABLE_ID -> "vnd.android.cursor.item/${DatabaseContract.AUTHORITY}.${DatabaseContract.MyTable.TABLE_NAME}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
       // dbHelper = MyDatabaseHelper(mContext!!)
        val db = dbHelper.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            MY_TABLE -> db.insert(DatabaseContract.MyTable.TABLE_NAME, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            MY_TABLE -> db.delete(DatabaseContract.MyTable.TABLE_NAME, selection, selectionArgs)
            MY_TABLE_ID -> {
                val id = ContentUris.parseId(uri)
                db.delete(DatabaseContract.MyTable.TABLE_NAME, "${DatabaseContract.MyTable}=?", arrayOf(id.toString()))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            MY_TABLE -> db.update(DatabaseContract.MyTable.TABLE_NAME, values, selection, selectionArgs)
            MY_TABLE_ID -> {
                val id = ContentUris.parseId(uri)
                db.update(DatabaseContract.MyTable.TABLE_NAME, values, "${DatabaseContract.MyTable}=?", arrayOf(id.toString()))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
