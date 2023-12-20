package com.capstone.basaliproject.ui.scan.history

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.databinding.FragmentTabHistoryBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import kotlinx.coroutines.launch

class TabHistoryFragment : Fragment() {
    private var _binding: FragmentTabHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NestedAdapter
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTabHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState == null) {
            //code goes below here

            adapter = NestedAdapter()
            val layoutManager = LinearLayoutManager(requireContext())
            binding.apply {
                rvHistory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                rvHistory.layoutManager = layoutManager
                rvHistory.setHasFixedSize(true)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TabHistoryFragment", "Observing history data...")

        if (adapter.itemCount == 0) {
            historyViewModel.history.observe(requireActivity()) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }
    }

}