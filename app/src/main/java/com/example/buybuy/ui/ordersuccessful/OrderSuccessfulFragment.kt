package com.example.buybuy.ui.ordersuccessful

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentOrderSuccessfulBinding
import com.example.buybuy.enums.ToastMessage
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.viewBinding


class OrderSuccessfulFragment : Fragment(R.layout.fragment_order_successful) {

    private val binding by viewBinding(FragmentOrderSuccessfulBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener{
            findNavController().navigate(R.id.action_orderSuccessful_to_mainFragment,null, NavOptions.navOptions3)
        }
        ToastMessage.SUCCESS.showToast(requireContext(),getString(R.string.alert_message_order_successful))

        handleOnBackPressed()
    }


    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.mainFragment,null,NavOptions.leftAnim)
                }
            })
    }

}