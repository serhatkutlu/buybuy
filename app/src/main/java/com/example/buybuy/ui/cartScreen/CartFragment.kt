package com.example.buybuy.ui.cartScreen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentCartBinding
import com.example.buybuy.enums.CartClickEnums
import com.example.buybuy.ui.cartScreen.adapter.CartAdapter
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CartFragment:Fragment(R.layout.fragment_cart) {

    private val binding : FragmentCartBinding by viewBinding(FragmentCartBinding::bind)

    private val viewModel : CartViewModel by viewModels()
    private val cartAdapter by lazy{
        CartAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handleOnBackPressed()
        initObservers()
        initUi()
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.mainFragment)
            }
        })
    }
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeCardItems() }
                launch { observeTotalPrice() }

            }
        }
    }

    private suspend fun observeTotalPrice() {
        viewModel.totalPrice.collect{
          binding.tvPriceNew.text=getString(R.string.currency_symbol,it.toString())
        }
    }

    private suspend fun observeCardItems() {
        viewModel.cartItems.collect {
            when(it){
                is Resource.Success->{
                    binding.progressBar.gone()
                    cartAdapter.submitList(it.data)
                }
                is Resource.Error->{
                    cartAdapter.submitList(emptyList())
                    binding.progressBar.gone()
                    requireContext().showToast(it.message)
                }
                is Resource.Loading->{
                    binding.progressBar.visible()
                }
                is Resource.Empty->{}
            }
        }
    }

    private fun initUi() {
        initRV()
        binding.buttonBuyNow.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment,null,NavOptions.navOptions1)
        }
    }

    private fun initRV() {
        with(binding.recyclerView){
            adapter = cartAdapter
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            cartAdapter.productCartCountClickListener=::addToCartClickListener
        }
    }
    private fun addToCartClickListener(isPlus:CartClickEnums, id:Int){
        when(isPlus){
            CartClickEnums.PLUS->{
                viewModel.addToCart(id)

            }
            CartClickEnums.MINUS->{
                viewModel.deleteFromCart(id)

            }
            CartClickEnums.DELETE->{
                viewModel.deleteProductFromCart(id)
        }

    }
}}