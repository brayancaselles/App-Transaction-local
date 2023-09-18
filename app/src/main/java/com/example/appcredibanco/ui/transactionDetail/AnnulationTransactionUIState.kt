package com.example.appcredibanco.ui.transactionDetail

sealed class AnnulationTransactionUIState {
    object Loading : AnnulationTransactionUIState()
    data class Success(val messageInfo: String) : AnnulationTransactionUIState()
    data class Error(val message: String) : AnnulationTransactionUIState()
}
