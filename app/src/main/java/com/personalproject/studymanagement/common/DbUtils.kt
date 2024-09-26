package com.personalproject.studymanagement.common

import android.content.ContentValues
import android.content.Context

object DbUtils {
    fun createProjectTable(context: Context) {
        try {
            val dbHelper = MyDatabaseHelper(context)
            val db = dbHelper.writableDatabase

            // Example to create another table dynamically
            val tableName = "tbl_project"
            val columnNames = arrayOf(
                "id",
                "txt_name",
                "description",
                "txt_created_date",
                "txt_modified_date",
                "txt_status",
                "txt_priority",
                "txt_type",
                "txt_due_date"
            )
            val columnTypes = arrayOf(
                "INTEGER",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)",
                "VARCHAR(255)"
            )
            val constraints = arrayOf(
                "PRIMARY KEY AUTOINCREMENT",
                "NOT NULL",
                "NULL",
                "NOT NULL",
                "NOT NULL",
                "NOT NULL",
                "NOT NULL",
                "NOT NULL",
                "NOT NULL"
            )

            dbHelper.createTable(db, tableName, columnNames, columnTypes, constraints)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertProjectData(context: Context, values: ContentValues) {
        try {
            val dbHelper = MyDatabaseHelper(context)
            val db = dbHelper.writableDatabase
            db.insert("tbl_project", null, values)
        } catch (e: Exception) {

        }
    }
}