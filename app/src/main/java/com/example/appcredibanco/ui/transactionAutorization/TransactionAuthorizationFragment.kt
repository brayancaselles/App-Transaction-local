package com.example.appcredibanco.ui.transactionAutorization

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appcredibanco.R
import com.example.appcredibanco.common.dismissKeyboard
import com.example.appcredibanco.common.loseFocusAfterAction
import com.example.appcredibanco.common.onTextChanged
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.data.model.TransactionViewState
import com.example.appcredibanco.databinding.FragmentTransactionAuthorizationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class TransactionAuthorizationFragment : Fragment() {

    private var _binding: FragmentTransactionAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionAuthorizationViewModel by viewModels()
    private var current: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionAuthorizationBinding.inflate(layoutInflater, container, false)
        binding.apply {
            editTextCommerceCode.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            editTextCommerceCode.onTextChanged { onFieldChanged() }
            editTextTerminalCode.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            editTextTerminalCode.onTextChanged { onFieldChanged() }
            editTextAmount.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            editTextAmount.onTextChanged { onFieldChanged() }
            editTextCard.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            editTextCard.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            editTextCard.onTextChanged { onFieldChanged() }
            editTextAmount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != current) {
                        editTextAmount.removeTextChangedListener(this)

                        val cleanString: String = s!!.replace("""[$,.]""".toRegex(), "")

                        val parsed = cleanString.toDouble()
                        val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                        current = formatted
                        editTextAmount.setText(formatted)
                        editTextAmount.setSelection(formatted.length)

                        editTextAmount.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            buttonAuthorize.setOnClickListener {
                it.dismissKeyboard()
                viewModel.onDataSelected(
                    editTextCommerceCode.text.toString(),
                    editTextTerminalCode.text.toString(),
                    editTextAmount.text.toString(),
                    editTextCard.text.toString(),
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    updateUI(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showDialog.collect { event ->
                    event.enum?.let { showDialog(it) }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showLoading.collect { event ->
                    event.showLoading?.let { showLoading(it) }
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            viewModel.onFieldsChanged(
                commerceCode = binding.editTextCommerceCode.text.toString(),
                terminalCode = binding.editTextTerminalCode.text.toString(),
                amount = binding.editTextAmount.text.toString(),
                card = binding.editTextCard.text.toString(),
            )
        }
    }

    private fun updateUI(viewState: TransactionViewState) {
        binding.apply {
            editTextCommerceCode.error =
                if (viewState.isValidCommerceCode) null else getString(R.string.transaction_authorization_text_validate_commerce_code)
            editTextTerminalCode.error =
                if (viewState.isValidTerminalCode) null else getString(R.string.transaction_authorization_text_validate_terminal_code)
            editTextAmount.error =
                if (viewState.isValidTerminalCode) null else getString(R.string.transaction_authorization_text_validate_amount)
            editTextCard.error =
                if (viewState.isValidTerminalCode) null else getString(R.string.transaction_authorization_text_validate_card)
        }
    }

    private fun showDialog(enum: EnumResponseService) {
        when (enum) {
            EnumResponseService.IS_SUCCESS -> {
                showMessageDialog(
                    R.string.transaction_authorization_text_title_dialog,
                    R.string.transaction_authorization_text_success_transaction,
                )
                /*binding.apply {
                    editTextCommerceCode.text?.let { it.clear() }
                    editTextTerminalCode.text?.let { it.clear() }
                    editTextAmount.text?.let { it.clear() }
                    editTextCard.text?.let { it.clear() }
                }*/
            }

            EnumResponseService.IS_FAILURE, EnumResponseService.IS_DEFAULT -> showMessageDialog(
                R.string.transaction_authorization_text_title_error_dialog,
                R.string.transaction_authorization_text_failure_transaction,
            )
        }
    }

    private fun showMessageDialog(
        title: Int,
        message: Int,
    ) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(title))
            .setMessage(getString(message))
            .setPositiveButton(android.R.string.ok) { view, _ ->
                view.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.isVisible = show
    }
}
