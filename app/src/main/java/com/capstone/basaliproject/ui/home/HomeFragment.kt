package com.capstone.basaliproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.pref.AksaraModel
import com.capstone.basaliproject.databinding.FragmentHomeBinding
import com.capstone.basaliproject.databinding.ItemAksaraHomeBinding
import com.capstone.basaliproject.databinding.LearnItemBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.learn.LearnViewModel
import com.capstone.basaliproject.ui.learn.model.LearnModel
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.utils.ListAksaraAdapter
import com.capstone.basaliproject.utils.SetupUtils.Companion.closeOnBackPressed
import com.dicoding.picodiploma.loginwithanimation.view.story.LearnAdapter

class HomeFragment : Fragment(), ListAksaraAdapter.ItemClickListener {
    private lateinit var rvAksara: RecyclerView
    private val list = ArrayList<AksaraModel>()
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var aksaraAdapter: ListAksaraAdapter

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter
        aksaraAdapter = ListAksaraAdapter(this)

        // Set the RecyclerView's adapter
        binding.rvAksara.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAksara.adapter = aksaraAdapter

        val learnViewModel = ViewModelProvider(this).get(LearnViewModel::class.java)

        // Observe the data from ViewModel and submit it to the adapter
        learnViewModel.learnData.observe(viewLifecycleOwner, { data ->
            aksaraAdapter.submitList(data)
        })

        closeOnBackPressed()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(items: LearnModel, view: ItemAksaraHomeBinding) {
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