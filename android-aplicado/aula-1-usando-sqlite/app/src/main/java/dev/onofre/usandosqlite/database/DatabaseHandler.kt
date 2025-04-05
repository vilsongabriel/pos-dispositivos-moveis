package dev.onofre.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.onofre.usandosqlite.entity.Cadastro


class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val TABLE_NAME = "cadastro"
        enum class Fields(val index: Int, val columnName: String) {
            ID(0, "_id"),
            NOME(1, "nome"),
            TELEFONE(2, "telefone");
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                ${Fields.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Fields.NOME.columnName} TEXT,
                ${Fields.TELEFONE.columnName} TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun <T> withDatabase(operation: (SQLiteDatabase) -> T): T {
        return writableDatabase.use { db ->
            operation(db)
        }
    }

    fun insert(cadastro: Cadastro): Long = withDatabase { db ->
        val values = ContentValues().apply {
            put(Fields.NOME.columnName, cadastro.nome)
            put(Fields.TELEFONE.columnName, cadastro.telefone)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun update(cadastro: Cadastro): Int = withDatabase { db ->
        val values = ContentValues().apply {
            put(Fields.NOME.columnName, cadastro.nome)
            put(Fields.TELEFONE.columnName, cadastro.telefone)
        }
        db.update(TABLE_NAME, values, "${Fields.ID.columnName} = ?", arrayOf(cadastro.id.toString()))
    }

    fun delete (id: Int): Int = withDatabase { db ->
        db.delete(TABLE_NAME, "${Fields.ID.columnName} = ?", arrayOf(id.toString()))
    }

    fun find (id: Int): Cadastro? = withDatabase { db ->
        db.query(TABLE_NAME, null, "_id=${id}", null, null, null, null).use { cursor ->
            if (cursor.moveToFirst()) {
                Cadastro(
                    id,
                    cursor.getString(Fields.NOME.index),
                    cursor.getString(Fields.TELEFONE.index)
                )
            } else null
        }
    }

    fun list (): List<Cadastro> = withDatabase { db ->
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        cursor.use {
            buildList {
                while (cursor.moveToNext()) {
                    add(Cadastro(
                        cursor.getInt(Fields.ID.index),
                        cursor.getString(Fields.NOME.index),
                        cursor.getString(Fields.TELEFONE.index)
                    ))
                }
            }
        }
    }


}