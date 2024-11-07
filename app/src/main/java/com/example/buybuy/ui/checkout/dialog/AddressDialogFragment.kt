package com.example.buybuy.ui.checkout.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.databinding.DialogCheckoutAddressListBinding
import com.example.buybuy.ui.checkout.adapter.CheckoutAddressAdapter
import com.example.buybuy.util.viewBinding

class AddressDialogFragment(
    private val list: List<AddressData>,
    private val onclicklistener: (AddressData) -> Unit
) : DialogFragment(R.layout.dialog_checkout_address_list) {

    private val binding: DialogCheckoutAddressListBinding by viewBinding(DialogCheckoutAddressListBinding::bind)
    private val rvAdapter: CheckoutAddressAdapter by lazy {
        CheckoutAddressAdapter(list,onclicklistener)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return  super.onCreateDialog(savedInstanceState).apply{
            //dialog?.window?.setBackgroundDrawableResource(R.drawable.layouth_bg)
            setCanceledOnTouchOutside(true)
            setCancelable(true)

        }




    }

    override fun onStart() {
        super.onStart()
        initUi()
        //setBackground()
        setDialogSize()
    }

    private fun initUi() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }
    }
    private fun setDialogSize() {
        val window: Window? = dialog?.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
    private fun setBackground() {
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(R.drawable.layout_bg)
    }
}