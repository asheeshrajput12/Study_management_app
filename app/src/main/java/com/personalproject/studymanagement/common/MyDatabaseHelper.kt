package com.personalproject.studymanagement.common

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "studies_database.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Initial database creation logic if needed
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade as needed
    }

    fun createTable(db: SQLiteDatabase, tableName: String, columnNames: Array<String>, columnTypes: Array<String>, constraints: Array<String>) {
        require(columnNames.size == columnTypes.size && columnNames.size == constraints.size) {
            "Column names, types, and constraints arrays must have the same length"
        }

        val createTableQuery = StringBuilder("CREATE TABLE IF NOT EXISTS $tableName (")

        for (i in columnNames.indices) {
            createTableQuery.append("${columnNames[i]} ${columnTypes[i]} ${constraints[i]}")
            if (i < columnNames.size - 1) {
                createTableQuery.append(", ")
            }
        }
        createTableQuery.append(");")

        db.execSQL(createTableQuery.toString())
    }
    // used this code for create table
    /*val dbHelper = DatabaseHelper(context)
    val db = dbHelper.writableDatabase

    // Example to create another table dynamically
    val tableName = "another_table"
    val columnNames = arrayOf("id", "title", "description")
    val columnTypes = arrayOf("INTEGER", "TEXT", "TEXT")
    val constraints = arrayOf("PRIMARY KEY AUTOINCREMENT", "NOT NULL", "")

    dbHelper.createTable(db, tableName, columnNames, columnTypes, constraints)*/

}
