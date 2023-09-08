package com.example.appcredibanco.common.retrofit

import com.example.appcredibanco.data.model.AnnulationTransactionResponse
import com.example.appcredibanco.data.model.TransactionResponse
import com.example.appcredibanco.domain.model.AnnulationTransactionModel
import com.example.appcredibanco.domain.model.TransactionModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("authorization")
    suspend fun createTransaction(
        @Body transactionData: TransactionModel,
    ): TransactionResponse

    @POST("annulment")
    suspend fun annulationTransaction(@Body transactionData: AnnulationTransactionModel): AnnulationTransactionResponse
}
