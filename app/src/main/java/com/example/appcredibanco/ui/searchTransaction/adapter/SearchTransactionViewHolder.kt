package com.example.appcredibanco.ui.searchTransaction.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appcredibanco.R
import com.example.appcredibanco.databinding.ItemSearchTransactionBinding
import com.example.appcredibanco.domain.model.Transaction

class SearchTransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemSearchTransactionBinding.bind(view)

    fun binding(transactionInfo: Transaction, onItemSelected: (Transaction) -> Unit) {
        val context = binding.textViewReceiptNumber.context

        binding.apply {
            textViewReceiptNumber.text = context.getString(
                R.string.transaction_list_text_receipt_number,
                transactionInfo.receiptNumber,
            )
            textViewReceiptNumber.setOnClickListener { onItemSelected(transactionInfo) }
        }
    }
}
