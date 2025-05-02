package dev.onofre.cashflow.model

data class Transaction (
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val type:  TransactionType,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)