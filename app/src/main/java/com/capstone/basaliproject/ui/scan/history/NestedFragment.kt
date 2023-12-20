package com.capstone.basaliproject.ui.scan.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.databinding.FragmentNestedBinding
import com.capstone.basaliproject.ui.ViewModelFactory

class NestedFragment : Fragment() {

    private var _binding: FragmentNestedBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NestedAdapter
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    var apiService: ApiService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNestedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.childRv
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = NestedAdapter()
        adapter.setNestedItemClickListener(object : NestedAdapter.NestedItemClickListener {
            override fun onItemClicked(item: DataItem) {

            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.getPagingData().observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_SCANNED_AT = "arg_scanned_at"

        fun newInstance(title: String, scannedAt: String): NestedFragment {
            val fragment = NestedFragment()
            var args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_SCANNED_AT, scannedAt)
            }
            fragment.arguments = args
            return fragment
        }
    }

}