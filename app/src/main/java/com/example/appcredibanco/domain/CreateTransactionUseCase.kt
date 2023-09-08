package com.example.appcredibanco.domain

import android.os.Build
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.data.TransactionRepository
import com.example.appcredibanco.domain.model.TransactionModel
import java.util.Base64
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {

    suspend fun create(
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String,
    ): EnumResponseService {
        val stringForAuthorization = commerceCode + terminalCode
        val authorizationBase64: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(stringForAuthorization.toByteArray())
        } else {
            android.util.Base64.encodeToString(
                stringForAuthorization.toByteArray(),
                android.util.Base64.DEFAULT,
            )
        }

        val amountReplace = amount.replace(Regex("[$,.]"), "")

        return repository.requestTransactionDataByTransactionInfoFromApi(
            TransactionModel(
                commerceCode = commerceCode,
                terminalCode = terminalCode,
                amount = amountReplace,
                card = card,
            ),
        )
    }
}
