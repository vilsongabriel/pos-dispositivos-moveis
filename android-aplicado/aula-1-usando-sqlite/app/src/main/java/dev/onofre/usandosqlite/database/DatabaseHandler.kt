package dev.onofre.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.onofre.usandosqlite.entity.Registration

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val TABLE_NAME = "registration"
        enum class Fields(val index: Int, val columnName: String) {
            ID(0, "_id"),
            NAME(1, "name"),
            PHONE(2, "phone")
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                ${Fields.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Fields.NAME.columnName} TEXT,
                ${Fields.PHONE.columnName} TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun <T> withDatabase(operation: (SQLiteDatabase) -> T): T {
        return writableDatabase.use { db ->
            operation(db)
        }
    }

    fun insert(registration: Registration): Long = withDatabase { db ->
        val values = ContentValues().apply {
            put(Fields.NAME.columnName, registration.name)
            put(Fields.PHONE.columnName, registration.phone)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun update(registration: Registration): Int = withDatabase { db ->
        val values = ContentValues().apply {
            put(Fields.NAME.columnName, registration.name)
            put(Fields.PHONE.columnName, registration.phone)
        }
        db.update(TABLE_NAME, values, "${Fields.ID.columnName} = ?", arrayOf(registration.id.toString()))
    }

    fun delete(id: Int): Int = withDatabase { db ->
        db.delete(TABLE_NAME, "${Fields.ID.columnName} = ?", arrayOf(id.toString()))
    }

    fun find(id: Int): Registration? = withDatabase { db ->
        db.query(TABLE_NAME, null, "${Fields.ID.columnName} = ?", arrayOf(id.toString()), null, null, null).use { cursor ->
            if (cursor.moveToFirst()) {
                Registration(
                    id,
                    cursor.getString(Fields.NAME.index),
                    cursor.getString(Fields.PHONE.index)
                )
            } else null
        }
    }

    fun list(): List<Registration> = withDatabase { db ->
        db.query(TABLE_NAME, null, null, null, null, null, null).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Registration(
                        cursor.getInt(Fields.ID.index),
                        cursor.getString(Fields.NAME.index),
                        cursor.getString(Fields.PHONE.index)
                    ))
                }
            }
        }
    }
}