package com.capstone.basaliproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.pref.AksaraModel
import com.capstone.basaliproject.databinding.FragmentHomeBinding
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.ui.signup.SignupActivity
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.capstone.basaliproject.utils.ListAksaraAdapter
import com.capstone.basaliproject.utils.SetupUtils.Companion.closeOnBackPressed

class HomeFragment : Fragment() {
    private lateinit var rvAksara: RecyclerView
    private val list = ArrayList<AksaraModel>()
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        rvAksara = root.findViewById(R.id.rv_aksara)
        rvAksara.setHasFixedSize(true)

        list.addAll(getListAksara())
        showRecyclerList()

        closeOnBackPressed()

        return root
    }

    private fun getListAksara(): ArrayList<AksaraModel> {
        val dataName = resources.getStringArray(R.array.aksara_name)
        val dataDescription = resources.getStringArray(R.array.aksara_descriptions)
        val dataPhoto = resources.obtainTypedArray(R.array.aksara_photo)
        val listAksara = ArrayList<AksaraModel>()
        for (i in dataName.indices) {
            val aksara = AksaraModel(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listAksara.add(aksara)
        }
        return listAksara
    }

    private fun showRecyclerList() {
        rvAksara.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listAksaraAdapter = ListAksaraAdapter(list)
        rvAksara.adapter = listAksaraAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}