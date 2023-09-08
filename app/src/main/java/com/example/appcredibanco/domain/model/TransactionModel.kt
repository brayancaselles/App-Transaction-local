package com.example.appcredibanco.domain.model

import java.util.UUID

data class TransactionModel(
    val id: String = UUID.randomUUID().toString(),
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
)

