package com.example.appcredibanco.ui.transactionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appcredibanco.databinding.FragmentTransactionListBinding
import com.example.appcredibanco.ui.transactionList.adapter.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionListViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionListBinding.inflate(layoutInflater, container, false)

        binding.apply {
            transactionAdapter = TransactionAdapter()
            recyclerViewTransaction.apply {
                layoutManager = GridLayoutManager(context, 1)
                adapter = transactionAdapter
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
    }

    private fun setListAdapter() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transaction.collect {
                    if (it.isNotEmpty()) {
                        transactionAdapter.updateList(it)
                        transactionAdapter.notifyDataSetChanged()
                    } else {
                        binding.recyclerViewTransaction.visibility = View.GONE
                        binding.textViewNoListTransaction.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
