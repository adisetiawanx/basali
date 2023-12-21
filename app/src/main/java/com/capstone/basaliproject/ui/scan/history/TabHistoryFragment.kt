package com.capstone.basaliproject.ui.scan.history

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.databinding.FragmentTabHistoryBinding
import com.capstone.basaliproject.ui.ViewModelFactory

class TabHistoryFragment : Fragment() {
    private var _binding: FragmentTabHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter
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

            historyViewModel.isLoading.observe(viewLifecycleOwner){
                showLoading(it)
            }

            adapter = HistoryAdapter()
            adapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback{
                override fun onItemClicked(data: DataItem) {

                }
            })

            val layoutManager = LinearLayoutManager(requireContext())
            binding.rvHistory.layoutManager = layoutManager
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.adapter = adapter


            historyViewModel.aksaraData.observe(viewLifecycleOwner){ listUsers ->
                if (listUsers.isNotEmpty()){
                    adapter.setList(listUsers)
                } else{
                    showWarning(getString(R.string.failed), getString(R.string.failed_to_get_history_data_failed_to_get_data_please_log_back_into_the_application_if_this_continues_to_happen))
                }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.searchHistory("")
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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}