package com.example.buybuy.ui.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentLoginBinding
import com.example.buybuy.ui.MainActivity
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.checkNullOrEmpty
import com.example.buybuy.util.checkEmail
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.showToast
import com.example.buybuy.util.visible
import com.example.buybuy.util.viewBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initUi()
        handleOnBackPressed()
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    requireContext().showAlertDialog(
                        getString(R.string.alert_title_exit),
                        getString(R.string.alert_message_exit),
                        positiveButtonAction = {
                            requireActivity().finish()
                        })
                }

            })
    }

    private fun initObserver() {
        with(loginViewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {

                        loginFlow.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    progressBar.visible()
                                }

                                is Resource.Success -> {
                                    progressBar.gone()
                                    findNavController().navigate(R.id.action_loginFragment_to_main_nav_graph)
                                }

                                is Resource.Error -> {
                                    progressBar.gone()
                                    buttonlogin.isEnabled = true
                                    context?.showToast(getString(R.string.invalid_email_password))
                                }

                                else -> {}
                            }
                        }


                    }
                }
            }
        }

    }

    private fun initUi() {
        with(binding) {
            with(loginViewModel) {
                buttonlogin.setOnClickListener {
                    if (etEmail.checkEmail(getString(R.string.invalid_email)) &&
                        etPassword.checkNullOrEmpty(getString(R.string.invalid_password))
                    ) {
                        loginEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        buttonlogin.isEnabled = false
                    }
                }

            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_signupFragment,
                    null,
                    navOptions = NavOptions.rightAnim
                )
            }
            llForgotPassword.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_forgotPasswordFragment,
                    null,
                    navOptions = NavOptions.rightAnim
                )
            }

        }

    }

}