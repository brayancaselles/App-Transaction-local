package com.example.appcredibanco.ui.searchTransaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcredibanco.R
import com.example.appcredibanco.domain.model.Transaction

class SearchTransactionAdapter(private val onItemSelected: (Transaction) -> Unit) :
    RecyclerView.Adapter<SearchTransactionViewHolder>() {

    private var transactionList: List<Transaction> = listOf()

    fun updateList(list: List<Transaction>) {
        transactionList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTransactionViewHolder {
        return SearchTransactionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_transaction, parent, false),
        )
    }

    override fun getItemCount(): Int = transactionList.size

    override fun onBindViewHolder(holder: SearchTransactionViewHolder, position: Int) {
        holder.binding(transactionList[position], onItemSelected)
    }
}
