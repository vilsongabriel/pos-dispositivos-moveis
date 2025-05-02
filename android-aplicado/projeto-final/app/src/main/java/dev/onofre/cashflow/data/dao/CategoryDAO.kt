package dev.onofre.cashflow.data.dao

import dev.onofre.cashflow.data.db.DbHelper
import dev.onofre.cashflow.model.Category
import dev.onofre.cashflow.model.TransactionType

class CategoryDAO (private val db: DbHelper) {
    fun getCategoriesByType(type: TransactionType): List<Category> {
        val db = db.writableDatabase
        return try {
            db.query(DbHelper.TABLE_CATEGORIES, null, "${DbHelper.COLUMN_CATEGORY_TYPE} = ?", arrayOf(type.name), null, null, null).use { cursor ->
                buildList {
                    while (cursor.moveToNext()) {
                        add(Category(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_CATEGORY_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_CATEGORY_NAME)),
                            type
                        ))
                    }
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}