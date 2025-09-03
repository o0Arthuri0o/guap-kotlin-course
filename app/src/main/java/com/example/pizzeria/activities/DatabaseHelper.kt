package com.example.pizzeria.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pizzeria.models.Pizza

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pizzeria.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PIZZAS = "pizzas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URI = "image_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_PIZZAS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_IMAGE_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PIZZAS")
        onCreate(db)
    }

    fun insertPizza(pizza: Pizza): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, pizza.name)
            put(COLUMN_DESCRIPTION, pizza.description)
            put(COLUMN_IMAGE_URI, pizza.imageUri)
        }
        return db.insert(TABLE_PIZZAS, null, values)
    }

    fun getAllPizzas(): List<Pizza> {
        val pizzas = mutableListOf<Pizza>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PIZZAS, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
            pizzas.add(Pizza(id, name, description, imageUri))
        }
        cursor.close()
        return pizzas
    }

    fun updatePizza(pizza: Pizza, id: Long): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, pizza.name)
            put(COLUMN_DESCRIPTION, pizza.description)
            put(COLUMN_IMAGE_URI, pizza.imageUri)
        }
        return db.update(TABLE_PIZZAS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deletePizza(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_PIZZAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}