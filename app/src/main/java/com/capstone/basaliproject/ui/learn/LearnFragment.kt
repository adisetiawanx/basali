package com.capstone.basaliproject.ui.learn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.databinding.FragmentLearnBinding

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val learnViewModel = ViewModelProvider(this).get(LearnViewModel::class.java)

        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

}