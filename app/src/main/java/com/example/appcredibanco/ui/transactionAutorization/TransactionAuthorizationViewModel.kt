package com.example.appcredibanco.ui.transactionAutorization

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

data class Event(var enum: EnumResponseService? = null)
data class Loading(var showLoading: Boolean? = null)

@HiltViewModel
class TransactionAuthorizationViewModel @Inject constructor(private val createTransactionUseCase: CreateTransactionUseCase) :
    ViewModel() {
    private companion object {
        const val EQUAL_CHARS_LENGTH = 6
        const val MIN_CHARS_LENGTH = 0.00
        const val MAX_CHARS_LENGTH = 16
    }

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> get() = _viewState

    private val _showDialog: MutableStateFlow<Event> by lazy { MutableStateFlow(Event()) }
    val showDialog: StateFlow<Event> get() = _showDialog

    private val _showLoading: MutableStateFlow<Loading> by lazy { MutableStateFlow(Loading()) }
    val showLoading: StateFlow<Loading> get() = _showLoading

    fun onFieldsChanged(commerceCode: String, terminalCode: String, amount: String, card: String) {
        _viewState.value = TransactionViewState(
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
            val enum = createTransactionUseCase.create(commerceCode, terminalCode, amount, card)
            _showDialog.update { currentState ->
                currentState.copy(
                    enum = enum,
                )
            }
            _showLoading.update { currentState ->
                currentState.copy(showLoading = false)
            }
        }
    }
}
