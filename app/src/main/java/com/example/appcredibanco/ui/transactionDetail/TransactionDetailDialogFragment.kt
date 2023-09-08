package com.example.appcredibanco.ui.transactionDetail

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appcredibanco.R
import com.example.appcredibanco.common.util.EnumResponseService
import com.example.appcredibanco.databinding.FragmentTransactionDetailDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionDetailDialogFragment() :
    DialogFragment() {

    private var _binding: FragmentTransactionDetailDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionDetailDialogViewModel by viewModels()

    private var idTransaction: Int? = null
    private lateinit var receiptNumber: String
    private lateinit var transactionIdentifier: String
    private lateinit var statusCode: String
    private lateinit var statusDescription: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionDetailDialogBinding.inflate(inflater, container, false)
        isCancelable = false
        binding.apply {
            arguments?.let { bundle ->
                idTransaction = bundle.getInt("id")
                receiptNumber = bundle.getString("receipt_number").toString()
                transactionIdentifier = bundle.getString("transaction_identifier").toString()
                statusCode = bundle.getString("status_code").toString()
                statusDescription = bundle.getString("status_description").toString()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
            )
            setGravity(Gravity.CENTER)
            attributes.windowAnimations = R.style.FullDialogAnimation
        }

        initDataText()
        annulationTransaction()
    }

    private fun initDataText() {
        binding.apply {
            textViewReceiptNumber.text = receiptNumber
            textViewTransactionIdentifier.text = transactionIdentifier
            textViewStatusCode.text = statusCode
            textViewStatusDescription.text = statusDescription
            imageButtonCancel.setOnClickListener { dismiss() }
            buttonVoidTransaction.setOnClickListener {
                showLoading(true)
                idTransaction?.let { id ->
                    viewModel.onDataSelected(
                        idTransaction = id,
                        receiptNumber = receiptNumber,
                        transactionIdentifier = transactionIdentifier,
                    )
                }
            }
        }
    }

    private fun annulationTransaction() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showDialog.collect { event ->
                    showLoading(false)
                    event.enum?.let { showDialog(it) }
                }
            }
        }
    }

    private fun showDialog(enum: EnumResponseService) {
        when (enum) {
            EnumResponseService.IS_SUCCESS -> showMessageDialog(
                R.string.transaction_authorization_text_title_dialog,
                R.string.transaction_authorization_text_success_transaction,
            )

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
