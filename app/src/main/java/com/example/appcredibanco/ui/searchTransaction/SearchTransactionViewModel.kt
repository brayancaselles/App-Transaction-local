package com.example.appcredibanco.ui.searchTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.domain.GetTransactionUseCase
import com.example.appcredibanco.ui.transactionList.TransactionUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTransactionViewModel @Inject constructor(private val getAllTransactionUseCase: GetTransactionUseCase) :
    ViewModel() {

    private val _transactions =
        MutableStateFlow<TransactionUIState>(TransactionUIState.Loading)
    val transactions: StateFlow<TransactionUIState> = _transactions

    fun getTransactionList() {
        viewModelScope.launch {
            getAllTransactionUseCase.getAllTransaction()
                .catch {
                    _transactions.value =
                        it.message?.let { error -> TransactionUIState.Error(error) }!!
                }
                .flowOn(Dispatchers.Main)
                .collect { _transactions.value = TransactionUIState.Success(it) }
        }
    }
}
