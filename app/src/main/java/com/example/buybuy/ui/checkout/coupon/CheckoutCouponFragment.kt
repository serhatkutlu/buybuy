package com.example.buybuy.ui.checkout.coupon

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.buybuy.R
import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.databinding.FragmentCheckoutCouponBinding
import com.example.buybuy.ui.checkout.CheckOutViewModel
import com.example.buybuy.util.Resource
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import kotlinx.coroutines.launch

class CheckoutCouponFragment(): Fragment(R.layout.fragment_checkout_coupon) {

    private val binding: FragmentCheckoutCouponBinding by viewBinding(FragmentCheckoutCouponBinding::bind)
    private val viewModel: CheckOutViewModel by viewModels({requireParentFragment()})
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.couponData.collect{
                    when(it){
                        is Resource.Success -> {
                            val coupons= it.data?.toMutableList()?.apply{
                                add(CouponData())
                            }
                            binding.spinner.adapter=CheckoutCouponSpinnerAdapter(requireContext(),coupons ?: listOf())
                            binding.spinner.setSelection(coupons?.size?.minus(1)?:0)
                        }
                        else -> {

                        }

                    }

                }
            }
        }
    }

    private fun initUi() {
        binding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = binding.spinner.getItemAtPosition(p2) as CouponData
                viewModel.updateTotalPrice(0f)
                if (selectedItem.used){
                    if ( selectedItem.discount!=null) {
                        requireContext().showToast(requireContext().getString(R.string.fragment_checkout_coupon_message))
                    }
                    binding.tvTotalPrice.text=requireContext().getString(R.string.currency_symbol_detail,viewModel.totalPrice.value)
                    binding.tvDiscountPrice.text=requireContext().getString(R.string.currency_symbol,0f)
                    viewModel.couponId=null
                }
                else{
                    selectedItem.discount?.let{
                        val discountPrice=viewModel.totalPrice.value*(it/100)
                        val newPrice=viewModel.totalPrice.value-discountPrice
                        binding.tvDiscountPrice.text=requireContext().getString(R.string.currency_symbol_detail_minus,discountPrice)
                        binding.tvTotalPrice.text=requireContext().getString(R.string.currency_symbol_detail,newPrice)
                        viewModel.updateTotalPrice(newPrice)
                        viewModel.couponId=selectedItem.id

                    }

                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }
}