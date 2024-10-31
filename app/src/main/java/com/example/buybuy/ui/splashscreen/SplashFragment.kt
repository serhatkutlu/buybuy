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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewmodel: SplashViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    delay(2000)
                    viewmodel.checkUserLogin.collect{

                        if (it){
                            findNavController().navigate(R.id.action_splashFragment_to_main_nav_graph, null,NavOptions.rightAnim)
                        }else{
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment, null,NavOptions.rightAnim)
                        }
                    }
                }
            }

        }
    }
}