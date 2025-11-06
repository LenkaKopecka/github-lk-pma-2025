package com.example.mybudget

data class TransactionData(
    val id: Int,
    val amount: Double,
    val note: String
)
// slouží k předávání transakce mezi kódem a databází