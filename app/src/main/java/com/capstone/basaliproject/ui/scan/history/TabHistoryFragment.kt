package com.capstone.basaliproject.ui.scan.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentTabHistoryBinding

class TabHistoryFragment : Fragment() {
    private var _binding: FragmentTabHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var mList: List<DataModel>
    private lateinit var adapter: ItemAdapter
    val mutableList = ArrayList<DataModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTabHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //code goes below here
        recyclerView = binding.rvHistoryParent
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mList = ArrayList()

        val nestedList1 = listOf(
            "Jams and Honey", "Pickles and Chutneys", "Readymade Meals",
            "Chyawanprash and Health Foods", "Pasta and Soups", "Sauces and Ketchup",
            "Namkeen and Snacks", "Honey and Spreads"
        )


        mutableList.add(DataModel(nestedList1, "January"))
        mutableList.add(DataModel(nestedList1, "February"))
        mutableList.add(DataModel(nestedList1, "March"))
        mutableList.add(DataModel(nestedList1, "April"))
        mutableList.add(DataModel(nestedList1, "May"))
        mutableList.add(DataModel(nestedList1, "June"))
        mutableList.add(DataModel(nestedList1, "July"))
        mutableList.add(DataModel(nestedList1, "August"))
        mutableList.add(DataModel(nestedList1, "September"))
        mutableList.add(DataModel(nestedList1, "October"))
        mutableList.add(DataModel(nestedList1, "November"))
        mutableList.add(DataModel(nestedList1, "Dececmber"))

        mList = mutableList.toList()

        adapter = ItemAdapter(mList)
        recyclerView.adapter = adapter

        return root
    }

}