package com.example.appcredibanco.ui.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.domain.GetTransactionUseCase
import com.example.appcredibanco.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(private val getAllTransactionUseCase: GetTransactionUseCase) :
    ViewModel() {

    private var _transaction = MutableStateFlow<List<Transaction>>(emptyList())
    val transaction: StateFlow<List<Transaction>> get() = _transaction

    init {
        viewModelScope.launch {
            _transaction.value = getAllTransactionUseCase.getAllTransaction()
        }
    }
}
