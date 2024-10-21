package com.example.buybuy.ui.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentForgotPasswordBinding
import com.example.buybuy.util.checkEmail

import com.example.buybuy.util.Constant.UNKNOWN_ERROR
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.showToast
import com.example.buybuy.util.visible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding: FragmentForgotPasswordBinding by viewBinding(FragmentForgotPasswordBinding::bind)
    private val viewmodel: ForgotPasswordViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        initUi()

    }

    private fun observe() {
        with(binding) {
            with(viewmodel) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                        resetFlow.collect {
                            when (it) {
                                is Resource.Loading -> progressBar.visible()
                                is Resource.Success -> {
                                    progressBar.visible()
                                    requireContext().showAlertDialog(
                                        getString(R.string.alert_title_reset_password),
                                        getString(R.string.alert_message_reset_password),
                                        getString(R.string.alert_reset_password_open_email),
                                        ::openEmailIntent,
                                        getString(R.string.alert_cancel)
                                    )
                                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                                }

                                is Resource.Error -> {
                                    requireContext().showToast(it.message)
                                    progressBar.gone()

                                }
                                is Resource.Empty -> {

                                }
                            }
                        }
                    }
                }
            }
        }
    }




    private fun openEmailIntent() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }

    private fun initUi() {
        with(binding) {
            with(viewmodel) {
                ivBack.setOnClickListener {
                    findNavController().navigateUp()
                }

                buttonSend.setOnClickListener {
                    if (etEmail.checkEmail(UNKNOWN_ERROR)) {
                        viewmodel.resetPasswordWithEmail(etEmail.text.toString())
                    }
                }
            }
        }

    }

}