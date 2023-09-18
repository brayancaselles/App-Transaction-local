package com.example.appcredibanco.data

import com.example.appcredibanco.common.retrofit.ApiService
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.data.database.dao.TransactionDao
import com.example.appcredibanco.data.database.entities.TransactionEntity
import com.example.appcredibanco.data.database.entities.toDataBase
import com.example.appcredibanco.domain.model.AnnulationTransactionModel
import com.example.appcredibanco.domain.model.Transaction
import com.example.appcredibanco.domain.model.TransactionModel
import com.example.appcredibanco.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val apiService: ApiService,
    private val transactionDao: TransactionDao,
) {

    suspend fun requestTransactionDataByTransactionInfoFromApi(
        model: TransactionModel,
    ): EnumResponseService {
        var enum: EnumResponseService = EnumResponseService.IS_DEFAULT
        runCatching { apiService.createTransaction(model) }.onSuccess {
            insertTransaction(it.toDataBase())
            enum = EnumResponseService.IS_SUCCESS
        }.onFailure {
            enum = EnumResponseService.IS_FAILURE
        }
        return enum
    }

    private suspend fun insertTransaction(transactions: TransactionEntity) {
        transactionDao.insertTransaction(transactions)
    }

    suspend fun getAllTransactionFromDataBase(): Flow<List<Transaction>> = flow {
        val response = transactionDao.getAllTransaction()
        emit(response.map { it.toDomain() })
    }

    suspend fun requestAnnulationTransaction(
        idTransaction: Int,
        model: AnnulationTransactionModel,
    ): EnumResponseService {
        var enum: EnumResponseService = EnumResponseService.IS_DEFAULT
        runCatching { apiService.annulationTransaction(model) }.onSuccess {
            deleteTransaction(idTransaction)
            enum = EnumResponseService.IS_SUCCESS
        }.onFailure { enum = EnumResponseService.IS_FAILURE }

        return enum
    }

    private suspend fun deleteTransaction(idTransaction: Int) {
        transactionDao.deleteTransaction(idTransaction)
    }
}
