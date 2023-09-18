package com.example.appcredibanco.ui.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.domain.GetTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(private val getAllTransactionUseCase: GetTransactionUseCase) :
    ViewModel() {

    private val _transactions =
        MutableStateFlow<TransactionUIState>(TransactionUIState.Loading)
    val transactions: StateFlow<TransactionUIState> = _transactions

    fun getTransactionList() {
        viewModelScope.launch {
            getAllTransactionUseCase.getAllTransaction()
                .catch {
                    it.message?.let { error ->
                        _transactions.value = TransactionUIState.Error(error)
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _transactions.value = TransactionUIState.Success(it)
                }
        }
    }
}
