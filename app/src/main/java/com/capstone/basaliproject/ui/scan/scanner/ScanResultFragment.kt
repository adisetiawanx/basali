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
import com.bumptech.glide.Glide
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentScanResultBinding

class ScanResultFragment : Fragment() {
    private var _binding: FragmentScanResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var imgView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanResultBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root
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
            Glide.with(requireContext())
                .load(imageUri)
                .into(imgView)
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val ARG_IMAGE_URI = "arg_image_uri"
    }
}