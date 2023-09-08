package com.example.appcredibanco.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appcredibanco.data.model.TransactionResponse

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "receipt_id") val receiptNumber: String,
    @ColumnInfo(name = "rrn") val transactionIdentifier: String,
    @ColumnInfo(name = "status_code") val statusCode: String,
    @ColumnInfo(name = "status_description") val statusDescription: String,
)

fun TransactionResponse.toDataBase() = TransactionEntity(
    receiptNumber = receiptNumber,
    transactionIdentifier = transactionIdentifier,
    statusCode = statusCode,
    statusDescription = statusDescription,
)
