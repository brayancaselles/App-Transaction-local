package com.example.appcredibanco.ui.transactionDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.domain.AnnulationTransactionUserCase
import com.example.appcredibanco.ui.transactionAutorization.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailDialogViewModel @Inject constructor(private val annulationTransactionUseCase: AnnulationTransactionUserCase) :
    ViewModel() {

    private val _showDialog: MutableStateFlow<Event> by lazy { MutableStateFlow(Event()) }
    val showDialog: StateFlow<Event> get() = _showDialog

    fun onDataSelected(
        idTransaction: Int,
        receiptNumber: String,
        transactionIdentifier: String,
    ) {
        viewModelScope.launch {
            val enum = annulationTransactionUseCase.annulationTransaction(
                idTransaction,
                receiptNumber,
                transactionIdentifier,
            )
            _showDialog.update { currenstate ->
                currenstate.copy(
                    enum = enum,
                )
            }
        }
    }
}
