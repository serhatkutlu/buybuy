package com.example.buybuy.ui.signup


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.buybuy.data.model.data.User
import com.example.buybuy.databinding.FragmentSignupBinding
import com.example.buybuy.util.CheckNullorEmpty
import com.example.buybuy.util.Checkemail
import com.example.buybuy.util.Constant.ALERT_CANCEL
import com.example.buybuy.util.Constant.ALERT_MESSAGE
import com.example.buybuy.util.Constant.ALERT_OK
import com.example.buybuy.util.Constant.ALERT_TITLE
import com.example.buybuy.util.Constant.PASSWORD_NOT_MATCH
import com.example.buybuy.util.Constant.PERMISSION_DENIED_GALLERY
import com.example.buybuy.util.Constant.PERMISSION_DENIED_SETTINGS
import com.example.buybuy.util.Constant.UNKNOWN_ERROR
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.showToast
import com.example.buybuy.util.Visible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val binding: FragmentSignupBinding by viewBinding(FragmentSignupBinding::bind)
    private val viewModel: SignupViewModel by viewModels()
    private var selectedimage: Uri? = null


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    selectedimage = selectedImageUri
                    binding.ivProfile.setImageURI(selectedImageUri)
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                requireContext().showToast(PERMISSION_DENIED_GALLERY)
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
                            binding.progressBar.Visible()
                        }

                        is Resource.Error -> {
                            binding.progressBar.Gone()
                            requireContext().showToast(it.message)
                        }

                        is Resource.Success -> {
                            binding.progressBar.Gone()
                            findNavController().navigate(R.id.action_signupFragment_to_main_nav_graph)
                        }
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
                if (etName.CheckNullorEmpty(UNKNOWN_ERROR) && etEmail.Checkemail(UNKNOWN_ERROR) && etPassword.CheckNullorEmpty(
                        UNKNOWN_ERROR
                    ) && etPassword2.CheckNullorEmpty(UNKNOWN_ERROR)
                ) {
                    if (etPassword.text.toString() == etPassword2.text.toString()) {
                        val user = User(
                            etEmail.text.toString(),
                            etName.text.toString(),
                            etPassword.text.toString(),
                            image = selectedimage
                        )
                        viewModel.Signup(user)

                    }
                    else{
                        requireContext().showToast(PASSWORD_NOT_MATCH)
                    }
                }

            }
            tvAlready.setOnClickListener {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
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
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requireContext().showAlertDialog(ALERT_TITLE,ALERT_MESSAGE,ALERT_OK,
                {requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) },
                ALERT_CANCEL,{openAppSettings(requireContext())})
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

}