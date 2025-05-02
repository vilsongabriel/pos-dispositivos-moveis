package dev.onofre.cashflow.data.dao

import android.content.ContentValues
import android.util.Log
import dev.onofre.cashflow.data.db.DbHelper
import dev.onofre.cashflow.model.Transaction
import dev.onofre.cashflow.model.TransactionType

class TransactionDAO (private val db: DbHelper) {
    fun add(transaction: Transaction): Long {
        val db = db.writableDatabase
        var id: Long = -1

        val values = ContentValues().apply {
            put(DbHelper.COLUMN_TRANSACTION_DESCRIPTION, transaction.description)
            put(DbHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(DbHelper.COLUMN_TRANSACTION_TYPE, transaction.type.name)
            put(DbHelper.COLUMN_TRANSACTION_CATEGORY, transaction.category)
            put(DbHelper.COLUMN_TRANSACTION_TIMESTAMP, transaction.timestamp)
        }

        try {
            id = db.insert(DbHelper.TABLE_TRANSACTIONS, null, values)
        } catch (e: Exception) {
            Log.e("TransactionDAO", "Erro ao criar transação", e)
        }

        return id
    }

    fun listAll(): List<Transaction> {
        val db = db.writableDatabase
        val orderBy = "${DbHelper.COLUMN_TRANSACTION_TIMESTAMP} DESC"
        return try {
            db.query(DbHelper.TABLE_TRANSACTIONS, null, null, null, null, null, orderBy).use { cursor ->
                buildList {
                    while (cursor.moveToNext()) {
                        add(Transaction(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_AMOUNT)),
                            TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_TYPE))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_CATEGORY)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TRANSACTION_TIMESTAMP))
                        ))
                    }
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}