package com.example.buybuy.ui.signup


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.databinding.FragmentSignupBinding
import com.example.buybuy.util.checkNullOrEmpty
import com.example.buybuy.util.checkEmail
import com.example.buybuy.util.Constant.UNKNOWN_ERROR
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.showToast
import com.example.buybuy.util.visible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val binding: FragmentSignupBinding by viewBinding(FragmentSignupBinding::bind)
    private val viewModel: SignupViewModel by viewModels()
    private var selectedimage: Uri? = null


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val selectedImageUri: Uri = uri

                selectedimage = selectedImageUri
                binding.ivProfile.setImageURI(selectedImageUri)

            }
        }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                requireContext().showToast(getString(R.string.permission_denied_gallery))
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        observeData()
    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressBar.visible()
                        }

                        is Resource.Error -> {
                            binding.progressBar.gone()
                            requireContext().showToast(it.message)
                            binding.buttonLogin.isEnabled = true
                        }

                        is Resource.Success -> {
                            binding.progressBar.gone()
                            findNavController().navigate(R.id.action_signupFragment_to_main_nav_graph)
                        }

                        is Resource.Empty -> {}
                    }
                }
            }
        }
    }

    private fun initUi() {
        with(binding) {

            ivProfile.setOnClickListener {
                checkPermissionAndOpenGallery()
            }

            ivClose.setOnClickListener {
                ivProfile.setImageResource(R.drawable.profile)
                selectedimage = null

            }

            buttonLogin.setOnClickListener {
                if (etName.checkNullOrEmpty(UNKNOWN_ERROR) && etEmail.checkEmail(UNKNOWN_ERROR) && etPassword.checkNullOrEmpty(
                        UNKNOWN_ERROR
                    ) && etPassword2.checkNullOrEmpty(UNKNOWN_ERROR)
                ) {
                    if (etPassword.text.toString() == etPassword2.text.toString()) {
                        val user = User(
                            etEmail.text.toString(),
                            etName.text.toString(),
                            etPassword.text.toString(),
                            image = selectedimage
                        )
                        viewModel.signup(user)
                        buttonLogin.isEnabled = false

                    } else {
                        requireContext().showToast(getString(R.string.password_not_match))
                    }
                }

            }
            tvAlready.setOnClickListener {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
            binding.ivBack.setOnClickListener{
                findNavController().popBackStack()
            }
        }


    }

    private fun checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            requireContext().showAlertDialog(getString(R.string.alert_title),
                getString(R.string.alert_message),
                getString(R.string.alert_ok),
                { requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) },
                getString(R.string.alert_cancel),
                { openAppSettings(requireContext()) })
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //pickImageLauncher.launch(intent)
        galleryLauncher.launch("image/*")
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

}