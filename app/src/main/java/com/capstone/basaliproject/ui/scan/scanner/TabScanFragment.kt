package com.capstone.basaliproject.ui.scan.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentTabScanBinding
import com.capstone.basaliproject.ui.scan.scanner.CameraActivity.Companion.CAMERAX_RESULT

class TabScanFragment : Fragment() {
    private var _binding: FragmentTabScanBinding? = null
    private val binding get() = _binding!!
    var path: String? = null
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(),
                    getString(R.string.permission_request_granted), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.permission_request_denied), Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(), REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabScanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val processButton = binding.processButton

        binding.captureButton.setOnClickListener {
            showCustomDialog()
        }

        processButton.setOnClickListener {
            if (currentImageUri != null) {
                // Process the image here
                Toast.makeText(requireContext(), "Image processing logic goes here", Toast.LENGTH_SHORT).show()
                showResultPopUp("Its Wa aksara", "We know because have a similaritis “ikutin metode” yang dipake",
                    currentImageUri!!
                )

            } else {
                // Show a message or take appropriate action when no image is selected
                showWarning("Select Image!", "You havent selected image, please select one to process")
            }
        }

        return root
    }

    private fun updateCaptureButtonText() {
        val captureButton = binding.captureButton
        val tvClearImg = binding.tvClearImage

        if (currentImageUri != null) {
            // Change the text to "Edit" when an image is selected
            captureButton.text = "Edit"
            tvClearImg.isEnabled = true
            tvClearImg.text = "Click Here To Reset"
            tvClearImg.setOnClickListener {
                //clear the selected image when click this textview
                currentImageUri = null
                binding.placeholder.setImageResource(R.drawable.cam_placeholder_logo)
                if (currentImageUri != null){
                    tvClearImg.isEnabled = false
                    tvClearImg.visibility = View.INVISIBLE
                }
                captureButton.text = "Capture"
                tvClearImg.text = ""
            }
        } else {
            // Reset the text to "Capture" when no image is selected
            captureButton.text = "Capture"
            tvClearImg.isEnabled = false
            tvClearImg.text = ""
        }
    }

    private fun showResultPopUp(titleFill: String, descFill: String, uri: Uri){
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_scan_result, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tvResultTitle)
        val desc = customView.findViewById<TextView>(R.id.tvResultDesc)
        val btnOk = customView.findViewById<Button>(R.id.btnDone)
        val img = customView.findViewById<ImageView>(R.id.ivResultScan)

        title.text = titleFill
        desc.text = descFill
        img.setImageURI(uri)

        btnOk.setOnClickListener {

        }
        val dialog = builder.create()
        btnOk.setOnClickListener {
            dialog.cancel()
        }
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    private fun showWarning(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_1_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnOk = customView.findViewById<Button>(R.id.ok_btn_id)

        title.text = titleFill
        desc.text = descFill

        btnOk.setOnClickListener {

        }
        val dialog = builder.create()
        btnOk.setOnClickListener {
            dialog.cancel()
        }
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_scan_option, null)
        builder.setView(customView)
        val dialog = builder.create()

        val cameraButton = customView.findViewById<ImageButton>(R.id.ib_camera)
        val galleryButton = customView.findViewById<ImageButton>(R.id.ib_gallery)
        val cancelButton = customView.findViewById<TextView>(R.id.btn_cancel)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        cameraButton.setOnClickListener{
            val intent = Intent(requireContext(), CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
            dialog.dismiss()
        }

        galleryButton.setOnClickListener{
            startGallery()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.placeholder.setImageURI(it)
            updateCaptureButtonText()
        }
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}