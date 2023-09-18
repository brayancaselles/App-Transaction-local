package com.example.appcredibanco.ui.transactionDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.domain.AnnulationTransactionUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailDialogViewModel @Inject constructor(private val annulationTransactionUseCase: AnnulationTransactionUserCase) :
    ViewModel() {

    private val _showDialog = MutableLiveData<EnumResponseService>()
    val showDialog: LiveData<EnumResponseService> = _showDialog

    fun onDataSelected(
        idTransaction: Int,
        receiptNumber: String,
        transactionIdentifier: String,
    ) {
        viewModelScope.launch {
            _showDialog.postValue(
                annulationTransactionUseCase.annulationTransaction(
                    idTransaction,
                    receiptNumber,
                    transactionIdentifier,
                ),
            )
        }
    }
}
