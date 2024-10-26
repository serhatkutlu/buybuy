package com.example.buybuy.ui.checkout.shippingaddress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentCheckoutShippingaddressBinding
import com.example.buybuy.ui.checkout.CheckOutViewModel
import com.example.buybuy.ui.checkout.dialog.AddressDialogFragment
import com.example.buybuy.util.Resource
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CheckoutShippingAddressFragment: Fragment(R.layout.fragment_checkout_shippingaddress) {


    private val binding: FragmentCheckoutShippingaddressBinding by viewBinding(FragmentCheckoutShippingaddressBinding::bind)
    private val viewmodel: CheckOutViewModel by viewModels({requireParentFragment()})
    private lateinit var dialogFragment: DialogFragment
    private var onClickListener={}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getAddressData()

        initObservers()
        initUi()
    }

    private fun initUi() {
        binding.llAddress.setOnClickListener{onClickListener()}

        binding.tvEditAddresses.setOnClickListener{findNavController().navigate(R.id.action_checkoutFragment_to_addressFragment)}
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.addresses.collect{
                    observeAddressData()
                }
            }
        }
    }

    private suspend fun observeAddressData() {
        viewmodel.addresses.collect { data ->
            when (data) {
                is Resource.Success -> {
                    onClickListener={
                        dialogFragment= AddressDialogFragment(data.data!!){
                            binding.tvNameSurname.text=it.name+" "+it.surname
                            binding.tvAddress.text=it.address
                            dialogFragment.dismiss()
                            viewmodel.lastAddressId=it.id
                        }
                        dialogFragment.show(parentFragmentManager, ADDRESS_DATA )

                    }
                    viewmodel.lastAddressId?.let{id->
                        data.data?.find{ it.id==id}?.let{lastAddress->
                            binding.tvNameSurname.text=lastAddress.name+" "+lastAddress.surname
                            binding.tvAddress.text=lastAddress.address
                        }

                    }
                }

                is Resource.Error -> {
                    onClickListener={
                        findNavController().navigate(R.id.action_checkoutFragment_to_addressFragment)
                    }
                }

                else -> Unit
            }
        }
    }

    companion object{
        private const val ADDRESS_DATA="addressData"

    }

}