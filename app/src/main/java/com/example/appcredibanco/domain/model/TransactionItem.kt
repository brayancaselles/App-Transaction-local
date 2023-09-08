package com.example.appcredibanco.domain.model

import com.example.appcredibanco.data.database.entities.TransactionEntity

data class Transaction(
    val id: Int,
    val receiptNumber: String,
    val transactionIdentifier: String,
    val statusCode: String,
    val statusDescription: String,
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    receiptNumber = receiptNumber,
    transactionIdentifier = transactionIdentifier,
    statusCode = statusCode,
    statusDescription = statusDescription,
)
