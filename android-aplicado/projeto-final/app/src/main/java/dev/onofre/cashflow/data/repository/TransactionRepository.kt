package dev.onofre.cashflow.data.repository

import dev.onofre.cashflow.data.dao.CategoryDAO
import dev.onofre.cashflow.data.dao.TransactionDAO
import dev.onofre.cashflow.model.Category
import dev.onofre.cashflow.model.Transaction
import dev.onofre.cashflow.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository (
    private val transactionDao: TransactionDAO,
    private val categoryDao: CategoryDAO
) {
    suspend fun addTransaction(transaction: Transaction): Long {
        return withContext(Dispatchers.IO) {
            transactionDao.add(transaction)
        }
    }

    suspend fun getAllTransactions(): List<Transaction> {
        return withContext (Dispatchers.IO) {
            transactionDao.listAll()
        }
    }

    suspend fun getCategoriesByType(type: TransactionType): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategoriesByType(type)
        }
    }

    suspend fun getCurrentBalance(): Double {
        return withContext(Dispatchers.IO) {
            val transactions = transactionDao.listAll()
            transactions.fold(0.0) { acc, transaction ->
                if (transaction.type == TransactionType.INCOME) {
                    acc + transaction.amount
                } else {
                    acc - transaction.amount
                }
            }
        }
    }
}
