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
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.capstone.basaliproject.R
import com.capstone.basaliproject.data.api.response.ProfileResponse
import com.capstone.basaliproject.databinding.FragmentSettingsBinding
import com.capstone.basaliproject.ui.logout.LogOutViewModel
import com.capstone.basaliproject.ui.ViewModelFactory
import com.capstone.basaliproject.ui.scan.scanner.CameraActivity
import com.capstone.basaliproject.ui.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SettingsFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private val viewModel by viewModels<LogOutViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModelSetting by viewModels<SettingsViewModel> {
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

        viewModelSetting.getProfileData()

        loadImage()

        viewModel.isLogOut.observe(requireActivity()) { isLogout ->
            if (isLogout) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            }
        }

        if (!isAdded) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupAction()

        binding.editProfile.setOnClickListener {
            showEditPictureDialog()
        }

        //display name and email user
        auth = FirebaseAuth.getInstance()
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        // Set tvName text to the logged account display name
        val user = auth.currentUser
        if (user != null) {
            val name = user.displayName
            val email = user.email
            tvName.text = name
            tvEmail.text = email

            val sharedPreferences = requireActivity().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
            val uriString = sharedPreferences.getString(uriKey, "")

            if (!uriString.isNullOrEmpty()) {
                val uri = Uri.parse(uriString)
                Glide.with(requireContext())
                    .load(uri)
                    .into(binding.profileImage)
            }
        }

        return root

    }

    private fun loadImage() {
        viewModelSetting.profileData.observe(viewLifecycleOwner) { profileResponse ->
            profileResponse?.let {
                showImage(it)
            }
        }
    }

    private fun setupAction() {
        binding.itemNotifications.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }
        binding.itemLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.itemMode.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_settings_to_themeFragment)
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
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        val drawButton = customView.findViewById<ImageButton>(R.id.ib_draw)
        val tvDraw = customView.findViewById<TextView>(R.id.tv_draw)
        val deleteButton = customView.findViewById<TextView>(R.id.btn_delete)
        val cancelButton = customView.findViewById<TextView>(R.id.btn_cancel)

        deleteButton.alpha = 1f
        drawButton.alpha = 0f
        tvDraw.alpha = 0f

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
//            clearImageData()
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
            loadImage()
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
            loadImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(profileResponse: ProfileResponse) {
        val profileData = profileResponse.data
        val photo = profileData?.photo

        if (photo != null) {
            val imageUrl = photo.url

            if (!imageUrl.isNullOrBlank()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.profileImage)
            }
        }
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}