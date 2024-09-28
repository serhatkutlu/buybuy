package com.example.buybuy.ui.address.newAddressScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentNewAddressBinding
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewAddressFragment: Fragment(R.layout.fragment_new_address) {

    private val binding:FragmentNewAddressBinding by viewBinding(FragmentNewAddressBinding::bind)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}