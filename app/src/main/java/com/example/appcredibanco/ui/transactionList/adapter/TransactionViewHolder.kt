package com.example.appcredibanco.ui.transactionList.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appcredibanco.R
import com.example.appcredibanco.databinding.ItemTransactionBinding
import com.example.appcredibanco.domain.model.Transaction

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTransactionBinding.bind(view)

    fun binding(transactionInfo: Transaction) {
        val context = binding.textViewTitle.context

        binding.apply {
            textViewReceiptNumber.text = context.getString(
                R.string.transaction_list_text_receipt_number,
                transactionInfo.receiptNumber,
            )

            textViewTransactionIdentifier.text = context.getString(
                R.string.transaction_list_text_identifier,
                transactionInfo.transactionIdentifier,
            )

            textViewStatusCode.text = context.getString(
                R.string.transaction_list_text_status_code,
                transactionInfo.statusCode,
            )

            textViewStatusDescription.text = transactionInfo.statusDescription
        }
    }
}
