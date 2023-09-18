package com.example.appcredibanco.ui.transactionList

import com.example.appcredibanco.domain.model.Transaction

sealed class TransactionUIState {
    object Loading : TransactionUIState()
    data class Success(val transactionList: List<Transaction>) : TransactionUIState()
    data class Error(val message: String) : TransactionUIState()
}
