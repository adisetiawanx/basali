package com.capstone.basaliproject.ui.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.R
import com.capstone.basaliproject.databinding.FragmentSettingsBinding
import com.capstone.basaliproject.ui.logout.LogOutViewModel
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.scan.scanner.CameraActivity
import com.capstone.basaliproject.ui.welcome.WelcomeActivity

class SettingsFragment : Fragment() {
    private val viewModel by viewModels<LogOutViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val sharedPrefsKey = "picture_data"
    private val uriKey = "profile_uri"
    private var currentImageUri: Uri? = null
    var path: String? = null

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
        val view = layoutInflater.inflate(R.layout.fragment_settings, container, false)

        // Check if the fragment is attached to an activity
        if (!isAdded) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            }
        }

        val settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupAction()

        loadImageData()

        // Setup profile picture
        binding.editProfile.setOnClickListener {
            showEditPictureDialog()
        }

        return root

    }

    private fun setupAction() {
        binding.itemNotifications.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }
        binding.itemLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.itemLogout.setOnClickListener {
            showCustomDialog(getString(R.string.warning), getString(R.string.yakin_ingin_logout))
        }
    }

    private fun showCustomDialog(titleFill: String, descFill: String) {
        val builder = AlertDialog.Builder(requireContext())

        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_2_option, null)
        builder.setView(customView)

        val title = customView.findViewById<TextView>(R.id.tv_title)
        val desc = customView.findViewById<TextView>(R.id.tv_desc)
        val btnYes = customView.findViewById<Button>(R.id.yes_btn_id)
        val btnNo = customView.findViewById<Button>(R.id.no_btn_id)

        title.text = titleFill
        desc.text = descFill

        btnYes.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            ViewModelFactory.refreshObject()
            startActivity(intent)
        }

        val dialog = builder.create()
        btnNo.setOnClickListener {
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showEditPictureDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_dialog_scan_option, null)
        builder.setView(customView)
        val dialog = builder.create()

        val cameraButton = customView.findViewById<ImageButton>(R.id.ib_camera)
        val galleryButton = customView.findViewById<ImageButton>(R.id.ib_gallery)
        val deleteButton = customView.findViewById<TextView>(R.id.btn_delete)
        val cancelButton = customView.findViewById<TextView>(R.id.btn_cancel)

        deleteButton.alpha = 1f

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

        deleteButton.setOnClickListener {
            clearImageData()
            dialog.dismiss()

            val defaultImageUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.w_hedgehog_hug}")
            binding.profileImage.setImageURI(defaultImageUri)
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
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
            binding.profileImage.setImageURI(it)

            saveImageUriToSharedPreferences(it.toString())
        }
    }

    private fun saveImageUriToSharedPreferences(uri: String) {
        val sharedPreferences = requireActivity().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(uriKey, uri)
        editor.apply()
    }

    private fun loadImageData() {
        val sharedPreferences = requireActivity().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString(uriKey, "")

        if (uriString != null) {
            if (uriString.isNotEmpty()) {
                val uri = Uri.parse(uriString)
                binding.profileImage.setImageURI(uri)
            }
        }
    }

    private fun clearImageData() {
        val sharedPreferences = requireActivity().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(uriKey)
        editor.apply()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}