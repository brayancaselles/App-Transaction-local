package com.example.appcredibanco.data.model

data class TransactionViewState(
    val isValidCommerceCode: Boolean = true,
    val isValidTerminalCode: Boolean = true,
    val isValidAmount: Boolean = true,
    val isValidCard: Boolean = true,
)
