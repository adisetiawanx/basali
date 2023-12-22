package com.capstone.basaliproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.api.response.ProfileResponse
import com.capstone.basaliproject.databinding.FragmentHomeBinding
import com.capstone.basaliproject.databinding.ItemAksaraHomeBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.learn.LearnViewModel
import com.capstone.basaliproject.ui.learn.model.LearnModel
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.capstone.basaliproject.utils.ListAksaraAdapter
import com.capstone.basaliproject.utils.SetupUtils.Companion.closeOnBackPressed
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeFragment : Fragment(), ListAksaraAdapter.ItemClickListener {

    private lateinit var aksaraAdapter: ListAksaraAdapter

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth : FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.getProfileData()

        homeViewModel.profileData.observe(viewLifecycleOwner) { profileResponse ->

            profileResponse?.let {
                showProfileImage(it)
            }

        }

        // Initialize the adapter
        aksaraAdapter = ListAksaraAdapter(this)

        // Set the RecyclerView's adapter
        binding.rvAksara.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAksara.adapter = aksaraAdapter
        val exploreImg = binding.ivExplore
        val btnHistory = binding.btnHistory

        btnHistory.setOnClickListener {
            findNavController().navigate(R.id.navigation_scan)
        }

        exploreImg.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("navItemId", R.id.navigation_learn)
            startActivity(intent)
        }

        val learnViewModel = ViewModelProvider(this)[LearnViewModel::class.java]

        // Observe the data from ViewModel and submit it to the adapter
        learnViewModel.learnData.observe(viewLifecycleOwner) { data ->
            aksaraAdapter.submitList(data)
        }

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

    fun showProfileImage(profileResponse: ProfileResponse) {
        val profileData = profileResponse.data
        val photo = profileData?.photo

        if (photo != null) {
            val imageUrl = photo.url

            if (!imageUrl.isNullOrBlank()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.ivProfile)
            }
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

    override fun onStart() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            if (!currentUser.isEmailVerified) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                FirebaseAuth.getInstance().signOut()
            }
        } else {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }
        super.onStart()
    }

}