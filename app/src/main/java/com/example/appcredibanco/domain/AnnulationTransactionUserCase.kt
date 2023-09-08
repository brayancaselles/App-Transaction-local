package com.example.appcredibanco.domain

import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.data.TransactionRepository
import com.example.appcredibanco.domain.model.AnnulationTransactionModel
import javax.inject.Inject

class AnnulationTransactionUserCase @Inject constructor(private val repository: TransactionRepository) {

    suspend fun annulationTransaction(
        idTransaction: Int,
        receiptNumber: String,
        transactionIdentifier: String,
    ): EnumResponseService {
        return repository.requestAnnulationTransaction(
            idTransaction,
            AnnulationTransactionModel(receiptId = receiptNumber, rrn = transactionIdentifier),
        )
    }
}
