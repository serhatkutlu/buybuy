package com.example.buybuy.ui.checkout

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
import com.example.buybuy.databinding.FragmentCheckoutBinding
import com.example.buybuy.ui.checkout.adapter.checkoutCartAdapter
import com.example.buybuy.util.Resource
import com.example.buybuy.util.invisible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import com.example.buybuy.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CheckoutFragment:Fragment(R.layout.fragment_checkout) {

    private val binding:FragmentCheckoutBinding by viewBinding(FragmentCheckoutBinding::bind)
    private val viewmodel:CheckOutViewModel by viewModels()
    private  val  adapter by lazy {
        checkoutCartAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.cartProducts.collect{
                    when(it){
                        is Resource.Success->{
                            adapter.updateList(it.data?: listOf())
                            binding.progressBar.invisible()
                            binding.scrollView2.visible()
                        }
                        is Resource.Error->{
                            binding.progressBar.invisible()
                            binding.scrollView2.invisible()
                            requireContext().showAlertDialog(getString(R.string.unknown_error),it.message?:"",positiveButtonAction = {
                                findNavController().popBackStack()
                            })
                        }
                        is Resource.Loading->{
                            binding.progressBar.visible()
                            binding.scrollView2.invisible()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initUi() {
        initRv()
    }

    private fun initRv() {
        binding.includedItemCart.recyclerView.adapter=adapter
        binding.includedItemCart.recyclerView.setHasFixedSize(true)
        binding.includedItemCart.recyclerView.layoutManager=
            LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

    }


}