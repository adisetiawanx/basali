package com.capstone.basaliproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentHomeBinding
import com.capstone.basaliproject.databinding.ItemAksaraHomeBinding
import com.capstone.basaliproject.ui.learn.LearnViewModel
import com.capstone.basaliproject.ui.learn.model.LearnModel
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.capstone.basaliproject.utils.ListAksaraAdapter
import com.capstone.basaliproject.utils.SetupUtils.Companion.closeOnBackPressed
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), ListAksaraAdapter.ItemClickListener {

    private lateinit var aksaraAdapter: ListAksaraAdapter

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth : FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter
        aksaraAdapter = ListAksaraAdapter(this)

        // Set the RecyclerView's adapter
        binding.rvAksara.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAksara.adapter = aksaraAdapter

        val learnViewModel = ViewModelProvider(this)[LearnViewModel::class.java]

        // Observe the data from ViewModel and submit it to the adapter
        learnViewModel.learnData.observe(viewLifecycleOwner) { data ->
            aksaraAdapter.submitList(data)
        }

        historyButton()

        closeOnBackPressed()

        auth = FirebaseAuth.getInstance()

        val tvName = binding.tvName
        // Set tvName text to the logged account display name
        val user = auth.currentUser
        if (user != null) {
            val name = user.displayName
            tvName.text = name
        }

        return root
    }

    private fun historyButton() {
        binding.btnHistory.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_learnDetailFragment)
        }
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