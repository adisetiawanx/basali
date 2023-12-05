package com.capstone.basaliproject.ui.scan.scanner

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentScanResultBinding
import com.capstone.basaliproject.databinding.FragmentTabScanBinding

class ScanResultFragment : Fragment() {
    private var _binding: FragmentScanResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var imgView: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var btnDone: Button

    companion object {
        const val ARG_IMAGE_URI = "arg_image_uri"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanResultBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Inflate the layout for this fragment

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInfoDialog()
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customView = layoutInflater.inflate(R.layout.fragment_scan_result, null)
        builder.setView(customView)
        imgView = binding.ivResultScan

        val imageUri = arguments?.getParcelable<Uri>(ARG_IMAGE_URI)

        // Set the image to the ImageView
        if (imageUri != null) {
            imgView.setImageURI(imageUri)
        }

        val dialog = builder.create()
        dialog.show()
    }
}