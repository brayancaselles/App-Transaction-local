package com.example.appcredibanco.domain

import com.example.appcredibanco.data.TransactionRepository
import com.example.appcredibanco.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {

    suspend fun getAllTransaction(): Flow<List<Transaction>> {
        return repository.getAllTransactionFromDataBase()
    }
}
