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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
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
        //code goes below here
        recyclerView = binding.rvHistoryParent
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ItemAdapter()
        adapter.setItemClickListener(object : ItemAdapter.ItemClickListener {
            override fun onItemClicked(item: DataItem) {
                showNestedItem(item)
            }
        })
        recyclerView.adapter = adapter

        return root
    }

    private fun showNestedItem(item: DataItem) {

        lifecycleScope.launch {
            val apiService = historyViewModel.getApiServiceWithToken()

            if (apiService != null) {
                try {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val nestedFragment = NestedFragment.newInstance(
                        item.predictionResult ?: "",
                        item.scannedAt ?: ""
                    )

                    nestedFragment.apiService = apiService

                    fragmentManager.beginTransaction()
                        .replace(R.id.container_nested_fragment, nestedFragment)
                        .addToBackStack(null)
                        .commit()

                    binding.containerNestedFragment.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.e("TabHistoryFragment", "Error get history", e)
                    showWarning(getString(R.string.error),
                        getString(R.string.failed_to_get_history_data_failed_to_get_data_please_log_back_into_the_application_if_this_continues_to_happen))
                }
            } else {
                Log.d("TabHistoryFragment", "ApiService is null")
            }
        }

    }

    private fun showWarning(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnOk = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = titleFill
        desc.text = descFill

        btnOk.setOnClickListener {

        }
        val dialog = builder.create()
        btnOk.setOnClickListener {
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.getPagingData().observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

}