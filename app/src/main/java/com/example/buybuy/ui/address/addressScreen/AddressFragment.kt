package com.example.buybuy.ui.address.addressScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentAddressBinding
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressFragment: Fragment(R.layout.fragment_address){

    private  val binding:FragmentAddressBinding by viewBinding(FragmentAddressBinding::bind)
    private val  viewModel:AddressViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initUi() {

        initRV()

        with(binding){
            tvNew.setOnClickListener {
                findNavController().navigate(R.id.action_addressFragment_to_newAddressFragment,null,NavOptions.navOption1)
            }
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initRV() {

    }
}