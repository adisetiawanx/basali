package com.capstone.basaliproject.ui.learn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentLearnBinding
import com.capstone.basaliproject.databinding.LearnItemBinding
import com.capstone.basaliproject.ui.learn.model.LearnModel
import com.dicoding.picodiploma.loginwithanimation.view.story.LearnAdapter

class LearnFragment : Fragment(), LearnAdapter.ItemClickListener {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    private lateinit var learnAdapter: LearnAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter
        learnAdapter = LearnAdapter(this)

        // Set the RecyclerView's adapter
        binding.rvLearn.layoutManager = LinearLayoutManager(context)
        binding.rvLearn.adapter = learnAdapter

        val learnViewModel = ViewModelProvider(this).get(LearnViewModel::class.java)

        // Observe the data from ViewModel and submit it to the adapter
        learnViewModel.learnData.observe(viewLifecycleOwner, { data ->
            learnAdapter.submitList(data)
        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(items: LearnModel, view: LearnItemBinding) {
        // Handle item click here
        val bundle = Bundle().apply {
            putInt("id", items.id)
            putInt("image", items.image)
            putString("title", items.title)
            putString("desc", items.desc)
        }

        // Use findNavController to navigate to DetailFragment
        view.root.findNavController().navigate(R.id.learnDetailFragment, bundle)
    }
}
