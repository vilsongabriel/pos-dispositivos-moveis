package dev.onofre.cashflow.model

data class Category (
    val id: Long = 0,
    val name: String,
    val type: TransactionType
)
