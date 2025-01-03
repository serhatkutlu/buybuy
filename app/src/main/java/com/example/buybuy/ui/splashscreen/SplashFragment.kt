package com.example.buybuy.ui.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.ui.MainActivity
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.isNetworkAvailable
import com.example.buybuy.util.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash2) {

    private val viewmodel: SplashViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        handleOnBackPressed()

    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.checkUserLogin().collect {
                    delay(2000)
                    when (it) {
                        is Resource.Success -> {
                            if (it.data == true) {
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_main_nav_graph,
                                    null,
                                    NavOptions.navOptions4(R.id.splashFragment)
                                )
                            } else {
                                findNavController().navigate(R.id.action_splashFragment_to_loginFragment, null,NavOptions.navOptions4(R.id.splashFragment))

                            }

                        }
                        is Resource.Loading -> {

                        }
                        else -> {
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment, null,NavOptions.navOptions4(R.id.splashFragment))

                        }
                    }
                }
            }
        }

    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })
    }
}