package com.example.buybuy.ui.checkout.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentCheckoutCartBinding
import com.example.buybuy.ui.checkout.CheckOutViewModel
import com.example.buybuy.ui.checkout.adapter.CheckoutCartAdapter
import com.example.buybuy.util.Resource
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import kotlinx.coroutines.launch


class CheckoutCartFragment(): Fragment(R.layout.fragment_checkout_cart) {

    private val binding: FragmentCheckoutCartBinding by viewBinding(FragmentCheckoutCartBinding::bind)
    private val viewmodel: CheckOutViewModel by viewModels({requireParentFragment()})
    private val adapter by lazy {
        CheckoutCartAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeCartProducts()
                }
            }
        }
    }


    private suspend fun observeCartProducts() {
        viewmodel.cartProducts.collect {

            when (it) {
                is Resource.Success -> {
                    adapter.updateList(it.data ?: listOf())

                }

                is Resource.Error -> {
                    requireContext().showAlertDialog(
                        getString(R.string.unknown_error),
                        it.message ?: "",
                        positiveButtonAction = {
                            findNavController().popBackStack()
                        }, cancelable = false)
                }

                else -> {}
            }
        }
    }



    private fun initUi() {
        initRv()
    }

    private fun initRv() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }
}