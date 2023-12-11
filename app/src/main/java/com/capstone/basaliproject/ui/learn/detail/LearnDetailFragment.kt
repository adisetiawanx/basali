package com.capstone.basaliproject.ui.learn.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentLearnBinding
import com.capstone.basaliproject.databinding.FragmentLearnDetailBinding
import com.capstone.basaliproject.ui.learn.karakter.KarakterAdapter
import com.capstone.basaliproject.ui.learn.karakter.KarakterViewModel

class LearnDetailFragment : Fragment() {
    private var _binding: FragmentLearnDetailBinding? = null
    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var btnDone: Button
    private lateinit var viewModel: KarakterViewModel
    private lateinit var adapter: KarakterAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLearnDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //code goes below here
        // Get the data from the arguments
        val id = arguments?.getInt("id")
        val image = arguments?.getInt("image")
        val titleStr = arguments?.getString("title")
        val descStr = arguments?.getString("desc")

        // Initialize your views
        imageView = binding.ivImage
        title = binding.tvTitle
        desc = binding.tvDesc
        btnDone = binding.btnDone

        // Set the data to your views
        imageView.setImageResource(image ?: 0)
        title.text = titleStr
        desc.text = descStr

        // Handle button click
        btnDone.setOnClickListener {
            // Do something when button is clicked
        }

        // Initialize your ViewModel
        viewModel = ViewModelProvider(this).get(KarakterViewModel::class.java)

        // Initialize your RecyclerView and Adapter
        adapter = KarakterAdapter() // replace with your actual adapter
        binding.rvKarakter.layoutManager = GridLayoutManager(context, 3)
        binding.rvKarakter.adapter = adapter

        when (id) {
            1 -> viewModel.swaraData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
            2 -> viewModel.wyanjanaData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
            5 -> viewModel.gantunganData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
            6 -> viewModel.angkaData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
            7 -> viewModel.tengenanData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
            8 -> viewModel.ardhaswaraData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}