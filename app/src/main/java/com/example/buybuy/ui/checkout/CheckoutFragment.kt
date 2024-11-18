package com.example.buybuy.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.databinding.FragmentCheckoutBinding
import com.example.buybuy.ui.checkout.cardinformation.CheckoutCardInformationFragment
import com.example.buybuy.ui.checkout.cart.CheckoutCartFragment
import com.example.buybuy.ui.checkout.coupon.CheckoutCouponFragment
import com.example.buybuy.ui.checkout.shippingaddress.CheckoutShippingAddressFragment
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private val viewmodel: CheckOutViewModel by viewModels()
    private val binding by viewBinding(FragmentCheckoutBinding::bind)

    private lateinit var cartFragment: CheckoutCartFragment
    private lateinit var shippingAddressFragment: CheckoutShippingAddressFragment
    private lateinit var cardInformationFragment: CheckoutCardInformationFragment
    private lateinit var couponFragment: CheckoutCouponFragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()

    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewmodel.totalPrice.collect {
                    binding.tvTotalPriceValue.text =
                        getString(R.string.currency_symbol_detail, it)
                }

            }
        }
    }

    private fun initUi() {
        initCardInformationFragment()
        initShippingAddressFragment()
        initCheckoutCartFragment()
        initCouponFragment()

        binding.buttonConfirm.setOnClickListener {

            if (shippingAddressFragment.onConfirmButtonClicked() && cardInformationFragment.onConfirmButtonClicked()) {
                lifecycleScope.launch {
                    viewmodel.saveOrder(createOrderData())
                }
                findNavController().navigate(
                    R.id.action_checkoutFragment_to_orderSuccessful,
                    null,
                    NavOptions.navOptions3
                )
            }

        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun createOrderData(): List<OrderData> {
        val time = LocalDate.now().toString()
        val cartProducts = viewmodel.cartProducts.value
        return when (cartProducts) {
            is Resource.Success -> {
                val list = cartProducts.data?.map {
                    OrderData(
                        time = time,
                        data = it.id,
                        piece = it.pieceCount
                    )
                }
                 list?: listOf()
            }

            else -> {
                 listOf()
            }
        }
    }
        private fun initCheckoutCartFragment() {
            cartFragment = CheckoutCartFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.cart_container, cartFragment)
                .commit()
        }
    private fun initCouponFragment() {
        couponFragment = CheckoutCouponFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.coupon_container, couponFragment)
            .commit()
    }

        private fun initShippingAddressFragment() {
            shippingAddressFragment = CheckoutShippingAddressFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.shipping_address_container, shippingAddressFragment)
                .commit()
        }

        private fun initCardInformationFragment() {
            cardInformationFragment = CheckoutCardInformationFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.cart_information_container, cardInformationFragment)
                .commit()


        }


        interface CheckoutFragmentInterface {
            fun onConfirmButtonClicked(): Boolean
        }

    }