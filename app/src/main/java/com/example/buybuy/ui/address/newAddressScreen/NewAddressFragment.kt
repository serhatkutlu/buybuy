package com.example.buybuy.ui.address.newAddressScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.data.model.data.Address
import com.example.buybuy.databinding.FragmentNewAddressBinding
import com.example.buybuy.enums.ToastType
import com.example.buybuy.util.Resource

import com.example.buybuy.util.checkNullOrEmpty
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NewAddressFragment : Fragment(R.layout.fragment_new_address) {

    private val binding: FragmentNewAddressBinding by viewBinding(FragmentNewAddressBinding::bind)

    private val viewmodel: NewAddressViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectData()
        initUi()
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.saveAddressResponse.collect{
                    when(it){
                        is Resource.Loading -> {
                            binding.btnSave.isClickable=false
                        }
                        is Resource.Success -> {
                            ToastType.SUCCESS.showToast(requireContext())
                            findNavController().popBackStack()
                        }
                        is Resource.Error -> {
                            binding.btnSave.isClickable=true
                            requireContext().showToast(it.message.toString())
                            ToastType.ERROR.showToast(requireContext())
                        }
                        else ->{}
                    }
                }
            }
        }
    }

    private fun initUi() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName
            val surname = binding.etSurname
            val address = binding.etAddress
            val addressName = binding.etAddressName
            val phoneNumber = binding.etPhone

            if (name.checkNullOrEmpty(getString(R.string.empty_field)) &&
                address.checkNullOrEmpty(getString(R.string.empty_field)) &&
                addressName.checkNullOrEmpty(getString(R.string.empty_field))
            ) {
                val addressModel = Address(
                    address = address.text.toString(),
                    addressName = addressName.text.toString(),
                    name = name.text.toString(),
                    surname = surname.text.toString(),
                    phone = phoneNumber.text.toString()
                )
                viewmodel.savaAddress(addressModel)
            }
        }

    }


}