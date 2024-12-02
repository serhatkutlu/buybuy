package com.example.buybuy.ui.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.isNetworkAvailable
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
                                    NavOptions.rightAnim
                                )
                            } else {
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_loginFragment,
                                    null,
                                    NavOptions.rightAnim
                                )
                            }

                        }
                        is Resource.Loading -> {

                        }
                        else -> {
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment, null,NavOptions.rightAnim)

                        }
                    }
                }
            }
        }

    }

}