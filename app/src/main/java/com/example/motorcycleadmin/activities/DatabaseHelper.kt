package com.example.motorcycleadmin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.motorcycleadmin.models.Motorcycle

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "motorcycles.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MOTORCYCLES = "motorcycles"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URI = "image_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MOTORCYCLES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_IMAGE_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOTORCYCLES")
        onCreate(db)
    }

    fun insertMotorcycle(motorcycle: Motorcycle): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, motorcycle.name)
            put(COLUMN_DESCRIPTION, motorcycle.description)
            put(COLUMN_IMAGE_URI, motorcycle.imageUri)
        }
        return db.insert(TABLE_MOTORCYCLES, null, values)
    }

    fun getAllMotorcycles(): List<Motorcycle> {
        val motorcycles = mutableListOf<Motorcycle>()
        val db = readableDatabase
        val cursor = db.query(TABLE_MOTORCYCLES, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
            motorcycles.add(Motorcycle(id, name, description, imageUri))
        }
        cursor.close()
        return motorcycles
    }

    fun updateMotorcycle(motorcycle: Motorcycle, id: Long): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, motorcycle.name)
            put(COLUMN_DESCRIPTION, motorcycle.description)
            put(COLUMN_IMAGE_URI, motorcycle.imageUri)
        }
        return db.update(TABLE_MOTORCYCLES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteMotorcycle(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_MOTORCYCLES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
