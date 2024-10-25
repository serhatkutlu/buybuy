package com.example.buybuy.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentCheckoutBinding
import com.example.buybuy.ui.checkout.adapter.CheckoutCartAdapter
import com.example.buybuy.ui.checkout.dialog.AddressDialogFragment
import com.example.buybuy.util.Resource
import com.example.buybuy.util.invisible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import com.example.buybuy.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private val binding: FragmentCheckoutBinding by viewBinding(FragmentCheckoutBinding::bind)
    private val viewmodel: CheckOutViewModel by viewModels()
    private lateinit var dialogFragment:DialogFragment
    private val adapter by lazy {
        CheckoutCartAdapter()
    }

    private var onClickListener={}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getAddressData()
        initObservers()
        initUi()
    }



    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeCartProducts()
                }
                launch { observeAddressData() }


            }
        }
    }

    private suspend fun observeAddressData() {
        viewmodel.addresses.collect { data ->
            when (data) {
                is Resource.Success -> {
                    onClickListener={
                        dialogFragment=AddressDialogFragment(data.data!!){
                            binding.includedItemShippingAddress.tvNameSurname.text=it.name+" "+it.surname
                            binding.includedItemShippingAddress.tvAddress.text=it.address
                            dialogFragment.dismiss()
                            viewmodel.lastAddressId=it.id
                        }
                        dialogFragment.show(parentFragmentManager, ADDRESS_DATA )

                    }
                    viewmodel.lastAddressId?.let{id->
                        data.data?.find{ it.id==id}?.let{lastAddress->
                            binding.includedItemShippingAddress.tvNameSurname.text=lastAddress.name+" "+lastAddress.surname
                            binding.includedItemShippingAddress.tvAddress.text=lastAddress.address
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


    private suspend fun observeCartProducts() {
        viewmodel.cartProducts.collect {

            when (it) {
                is Resource.Success -> {
                    adapter.updateList(it.data ?: listOf())
                    binding.progressBar.invisible()
                    binding.scrollView2.visible()
                }

                is Resource.Error -> {
                    binding.progressBar.invisible()
                    binding.scrollView2.invisible()
                    requireContext().showAlertDialog(
                        getString(R.string.unknown_error),
                        it.message ?: "",
                        positiveButtonAction = {
                            findNavController().popBackStack()
                        })
                }

                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.scrollView2.invisible()
                }

                else -> {}
            }
        }
    }

    private fun initUi() {
        initRv()
        binding.includedItemShippingAddress.llAddress.setOnClickListener{onClickListener()}
        binding.includedItemShippingAddress.tvEditAddresses.setOnClickListener{findNavController().navigate(R.id.action_checkoutFragment_to_addressFragment)}
    }

    private fun initRv() {
        binding.includedItemCart.recyclerView.adapter = adapter
        binding.includedItemCart.recyclerView.setHasFixedSize(true)
        binding.includedItemCart.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }
companion object{
    private const val ADDRESS_DATA="addressData"

}

}