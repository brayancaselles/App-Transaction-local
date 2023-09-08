package com.example.appcredibanco.ui.searchTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appcredibanco.R
import com.example.appcredibanco.databinding.FragmentSearchTransactionBinding
import com.example.appcredibanco.domain.model.Transaction
import com.example.appcredibanco.ui.searchTransaction.adapter.SearchTransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchTransactionFragment : Fragment() {

    private var _binding: FragmentSearchTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchTransactionViewModel by viewModels()
    private lateinit var searchTransactionAdapter: SearchTransactionAdapter
    private var transactionList: MutableList<Transaction> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchTransactionBinding.inflate(layoutInflater, container, false)
        showLoading(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
        initRecycler()
        initTextFilter()
    }

    private fun setListAdapter() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transaction.collect() {
                    searchTransactionAdapter.updateList(it)
                    transactionList = it.toMutableList()
                    searchTransactionAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initRecycler() {
        binding.apply {
            searchTransactionAdapter = SearchTransactionAdapter {
                val bundle = Bundle()
                bundle.putInt("id", it.id)
                bundle.putString("receipt_number", it.receiptNumber)
                bundle.putString("transaction_identifier", it.transactionIdentifier)
                bundle.putString("status_code", it.statusCode)
                bundle.putString("status_description", it.statusDescription)
                findNavController().navigate(R.id.transactionDetailDialogFragment, bundle)
            }
            recyclerViewSearchTransaction.apply {
                layoutManager = GridLayoutManager(context, 1)
                adapter = searchTransactionAdapter
            }
            showLoading(false)
        }
    }

    private fun initTextFilter() {
        binding.editTextFilter.addTextChangedListener { receiptNumberFilter ->
            val transactionListFilter = transactionList.filter { transaction ->
                transaction.receiptNumber.lowercase()
                    .contains(receiptNumberFilter.toString().lowercase())
            }
            searchTransactionAdapter.updateList(transactionListFilter)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.viewStubCustomLoading.isVisible = show
    }
}
