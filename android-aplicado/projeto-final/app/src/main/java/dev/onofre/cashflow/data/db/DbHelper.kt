package dev.onofre.cashflow.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.onofre.cashflow.model.TransactionType
import androidx.core.database.sqlite.transaction

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CashFlowApp.db"

        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_TRANSACTION_ID = "_id"
        const val COLUMN_TRANSACTION_DESCRIPTION = "description"
        const val COLUMN_TRANSACTION_AMOUNT = "amount"
        const val COLUMN_TRANSACTION_TYPE = "type"
        const val COLUMN_TRANSACTION_CATEGORY = "category"
        const val COLUMN_TRANSACTION_TIMESTAMP = "timestamp"

        const val TABLE_CATEGORIES = "categories"
        const val COLUMN_CATEGORY_ID = "_id"
        const val COLUMN_CATEGORY_NAME = "name"
        const val COLUMN_CATEGORY_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTransactionsTableSQL = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TRANSACTION_DESCRIPTION TEXT NOT NULL,
                $COLUMN_TRANSACTION_AMOUNT REAL NOT NULL,
                $COLUMN_TRANSACTION_TYPE TEXT NOT NULL,
                $COLUMN_TRANSACTION_CATEGORY TEXT NOT NULL,
                $COLUMN_TRANSACTION_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()

        db?.execSQL(createTransactionsTableSQL)

        val createCategoriesTableSQL = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL UNIQUE,
                $COLUMN_CATEGORY_TYPE TEXT NOT NULL
            )
        """.trimIndent()

        db?.execSQL(createCategoriesTableSQL)

        populateInitialCategories(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {}

    private fun populateInitialCategories(db: SQLiteDatabase?) {
        if (db == null) return
        db.transaction() {
            val incomeCategories = listOf("Salário", "Extras")
            incomeCategories.forEach { name ->
                insertCategoryIfNotExists(this, name, TransactionType.INCOME)
            }

            val expenseCategories = listOf("Alimentação", "Transporte", "Saúde", "Moradia")
            expenseCategories.forEach { name ->
                insertCategoryIfNotExists(this, name, TransactionType.EXPENSE)
            }
        }
    }

    private fun insertCategoryIfNotExists(db: SQLiteDatabase, name: String, type: TransactionType) {
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, name)
            put(COLUMN_CATEGORY_TYPE, type.name)
        }
        db.insertWithOnConflict(TABLE_CATEGORIES, null, values, SQLiteDatabase.CONFLICT_IGNORE)
    }
}
