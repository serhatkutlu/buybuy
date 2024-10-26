package com.example.buybuy.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.buybuy.R
import com.example.buybuy.ui.checkout.cardinformation.CheckoutCardInformationFragment
import com.example.buybuy.ui.checkout.cart.CheckoutCartFragment
import com.example.buybuy.ui.checkout.shippingaddress.CheckoutShippingAddressFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private val viewmodel: CheckOutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()
    }

    private fun initUi() {
        initCardInformationFragment()
        initShippingAddressFragment()
        initCheckoutCartFragment()

    }

    private fun initCheckoutCartFragment() {
        val childFragment = CheckoutCartFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.cart_container, childFragment)
            .commit()
    }

    private fun initShippingAddressFragment() {
        val childFragment = CheckoutShippingAddressFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.shipping_address_container, childFragment)
            .commit()
    }

    private fun initCardInformationFragment() {
        val childFragment = CheckoutCardInformationFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.cart_information_container, childFragment)
            .commit()


    }




}