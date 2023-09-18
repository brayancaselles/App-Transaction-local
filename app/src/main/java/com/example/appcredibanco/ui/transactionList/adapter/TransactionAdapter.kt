package com.example.appcredibanco.ui.transactionList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcredibanco.R
import com.example.appcredibanco.domain.model.Transaction

class TransactionAdapter() : RecyclerView.Adapter<TransactionViewHolder>() {

    private var transactionList: List<Transaction> = listOf()

    fun updateList(list: List<Transaction>) {
        transactionList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false),
        )
    }

    override fun getItemCount(): Int = transactionList.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.binding(transactionList[position])
    }
}
