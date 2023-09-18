package com.example.appcredibanco.ui.transactionAutorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.data.model.TransactionViewState
import com.example.appcredibanco.domain.CreateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Loading(var showLoading: Boolean? = null)

@HiltViewModel
class TransactionAuthorizationViewModel @Inject constructor(private val createTransactionUseCase: CreateTransactionUseCase) :
    ViewModel() {
    private companion object {
        const val EQUAL_CHARS_LENGTH = 6
        const val MIN_CHARS_LENGTH = 0.00
        const val MAX_CHARS_LENGTH = 16
    }

    private val _viewFieldsState = MutableStateFlow(TransactionViewState())
    val viewFieldsState: StateFlow<TransactionViewState> get() = _viewFieldsState

    private val _showDialog = MutableLiveData<EnumResponseService>()
    val showDialog: LiveData<EnumResponseService> get() = _showDialog

    private val _showLoading: MutableStateFlow<Loading> by lazy { MutableStateFlow(Loading()) }
    val showLoading: StateFlow<Loading> get() = _showLoading

    fun onFieldsChanged(commerceCode: String, terminalCode: String, amount: String, card: String) {
        _viewFieldsState.value = TransactionViewState(
            isValidCommerceCode = isValidForSixChars(commerceCode),
            isValidTerminalCode = isValidForSixChars(terminalCode),
            isValidAmount = isValidForTwoChars(amount),
            isValidCard = isValidForSixteenChars(card),
        )
    }

    private fun isValidForSixChars(string: String): Boolean =
        string.length >= EQUAL_CHARS_LENGTH || string.isEmpty()

    private fun isValidForTwoChars(string: String): Boolean =
        string.length > MIN_CHARS_LENGTH || string.isEmpty()

    private fun isValidForSixteenChars(string: String): Boolean =
        string.length >= MAX_CHARS_LENGTH || string.isEmpty()

    fun onDataSelected(
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String,
    ) {
        if (isValidForSixChars(commerceCode) && isValidForSixChars(terminalCode) && isValidForTwoChars(
                amount,
            ) && isValidForSixteenChars(card)
        ) {
            createTransaction(commerceCode, terminalCode, amount, card)
        } else {
            onFieldsChanged(commerceCode, terminalCode, amount, card)
        }
    }

    private fun createTransaction(
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String,
    ) {
        viewModelScope.launch {
            _showLoading.update { currentState ->
                currentState.copy(showLoading = true)
            }
            _showDialog.postValue(
                createTransactionUseCase.create(
                    commerceCode,
                    terminalCode,
                    amount,
                    card,
                ),
            )
            _showLoading.update { currentState ->
                currentState.copy(showLoading = false)
            }
        }
    }
}
