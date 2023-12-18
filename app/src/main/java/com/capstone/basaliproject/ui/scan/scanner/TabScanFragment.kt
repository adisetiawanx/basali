package com.capstone.basaliproject.ui.scan.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentTabScanBinding
import com.capstone.basaliproject.ui.scan.scanner.CameraActivity.Companion.CAMERAX_RESULT
import java.io.File
import java.io.FileOutputStream

class TabScanFragment : Fragment() {
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
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
    ): View {
        _binding = FragmentTabScanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val processButton = binding.processButton

        binding.captureButton.setOnClickListener {
            showCustomDialog()
        }

        processButton.setOnClickListener {
            if (currentImageUri != null) {
                // Process the image here
                Toast.makeText(requireContext(),
                    getString(R.string.image_processing_logic_goes_here), Toast.LENGTH_SHORT).show()
                    showResultPopUp(
                        getString(R.string.its_wa_aksara),
                        getString(R.string.we_know_because_have_a_similaritis_ikutin_metode_yang_dipake),
                    currentImageUri!!
                )

            } else {
                // Show a message or take appropriate action when no image is selected
                showWarning(getString(R.string.select_image),
                    getString(R.string.you_havent_selected_image_please_select_one_to_process))
            }
        }

        return root
    }

    private fun updateCaptureButtonText() {
        val captureButton = binding.captureButton
        val tvClearImg = binding.tvClearImage

        if (currentImageUri != null) {
            // Change the text to "Edit" when an image is selected
            captureButton.text = getString(R.string.edit)
            tvClearImg.isEnabled = true
            tvClearImg.text = getString(R.string.click_here_to_reset)
            tvClearImg.setOnClickListener {
                //clear the selected image when click this textview
                currentImageUri = null
                binding.placeholder.setImageResource(R.drawable.cam_placeholder_logo)
                if (currentImageUri != null){
                    tvClearImg.isEnabled = false
                    tvClearImg.visibility = View.INVISIBLE
                }
                captureButton.text = getString(R.string.capture)
                tvClearImg.text = ""
            }
        } else {
            // Reset the text to "Capture" when no image is selected
            captureButton.text = getString(R.string.capture)
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
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        val drawButton = customView.findViewById<ImageButton>(R.id.ib_draw)

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

        drawButton.setOnClickListener {
            drawCustomDialog()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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


    //drawing
    private fun drawCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_draw_dialog, null)
        builder.setView(customView)

        val drawingView = customView.findViewById<com.mihir.drawingcanvas.drawingView>(R.id.drawing_view)
        val btnSave = customView.findViewById<Button>(R.id.btnSave)
        val reset = customView.findViewById<TextView>(R.id.tvReset)
        val dialog = builder.create()

        reset.setOnClickListener {
            drawingView.clearDrawingBoard()
            val color = R.color.HighlightDark
            drawingView.setBrushColor(color)
        }

        btnSave.setOnClickListener {
            // Enable the drawing cache
            drawingView.isDrawingCacheEnabled = true

            // Get the bitmap from the drawing cache
            val bitmap = drawingView.drawingCache

            // Create a ContentValues object
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, getRandomString(5))
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            // Get a Uri for the file
            val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // Open an OutputStream and write to the file
            if (uri != null) {
                requireContext().contentResolver.openOutputStream(uri).use { outputStream ->
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                        currentImageUri = uri
                        showImage()
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}