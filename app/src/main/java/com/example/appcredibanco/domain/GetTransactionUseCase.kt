package com.example.appcredibanco.domain

import com.example.appcredibanco.data.TransactionRepository
import com.example.appcredibanco.domain.model.Transaction
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {

    suspend fun getAllTransaction(): List<Transaction> {
        val transactions = repository.getAllTransactionFromDataBase()

        return if (!transactions.isNullOrEmpty()) {
            transactions
        } else {
            emptyList()
        }
    }
}
