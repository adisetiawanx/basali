package com.capstone.basaliproject.ui.scan.scanner

import android.Manifest
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
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentTabScanBinding
import com.capstone.basaliproject.reduceFileImage
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.scan.history.HistoryViewModel
import com.capstone.basaliproject.ui.scan.scanner.CameraActivity.Companion.CAMERAX_RESULT
import com.capstone.basaliproject.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class TabScanFragment : Fragment() {
    private var _binding: FragmentTabScanBinding? = null
    private val binding get() = _binding!!
    var path: String? = null
    private var currentImageUri: Uri? = null

    private val scanViewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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

        binding.captureButton.setOnClickListener {
            showCustomDialog()
        }

        binding.processButton.setOnClickListener {
            uploadImage()
        }

        if (savedInstanceState != null) {
            currentImageUri = savedInstanceState.getParcelable(KEY_CURRENT_IMAGE_URI)
            showImage()
        }

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scanViewModel.saveInstanceState(currentImageUri)
        outState.putParcelable(KEY_CURRENT_IMAGE_URI, currentImageUri)
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

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.placeholder.setImageURI(it)
            updateCaptureButtonText()
        }
    }

    private fun uploadImage() {
        if (currentImageUri != null) {
            // Process the image here
            val pb = binding.progressBar
            pb.visibility = View.VISIBLE

            lifecycleScope.launch {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "image", imageFile.name, requestImageFile
                    )
                    postImage(multipartBody)
                }
            }
        } else {
            // Show a message or take appropriate action when no image is selected
            showWarning(getString(R.string.select_image),
                getString(R.string.you_havent_selected_image_please_select_one_to_process))
        }
    }

    private fun postImage(file: MultipartBody.Part) {
        val pb = binding.progressBar
        pb.visibility = View.VISIBLE
        lifecycleScope.launch {
            val apiService = scanViewModel.getApiServiceWithToken()

            if (apiService != null) {
                try {
                    val response = apiService.postImageToScan(file)

                    val imageUrl = response.data?.imageUrl
                    val prediction = response.data?.prediction

                    if (!imageUrl.isNullOrEmpty() && !prediction.isNullOrEmpty()) {
                        pb.visibility = View.GONE
                        showResultPopUp(prediction, imageUrl)
                    } else {
                        pb.visibility = View.GONE
                        showWarning(getString(R.string.error), getString(R.string.invalid_scan_result))
                    }
                }
                catch (e: Exception) {
                    pb.visibility = View.GONE
                    Log.e("ScanViewModel", "Error posting image: ${e.message}")
                    showWarning(getString(R.string.error),
                        getString(R.string.failed_to_process_the_image))
                }
            }
        }
    }

    private fun showResultPopUp(titleFill: String, uri: String){
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_scan_result, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tvResultTitle)
        val btnOk = customView.findViewById<Button>(R.id.btnDone)
        val img = customView.findViewById<ImageView>(R.id.ivResultScan)

        title.text = titleFill

        Glide.with(requireContext())
            .load(currentImageUri)
            .into(img)

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


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }


    //drawing
    private fun drawCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_draw_dialog, null)
        builder.setView(customView)

        val drawingView = customView.findViewById<com.mihir.drawingcanvas.drawingView>(R.id.drawing_view)
        val btnSave = customView.findViewById<Button>(R.id.btnSave)
        val btnCancel = customView.findViewById<Button>(R.id.btnCancel)
        val reset = customView.findViewById<TextView>(R.id.tvReset)
        val dialog = builder.create()

        reset.setOnClickListener {
            drawingView.clearDrawingBoard()
            val color = R.color.HighlightDark
            drawingView.setBrushColor(color)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
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


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val KEY_CURRENT_IMAGE_URI = "key_current_image_uri"
    }


}