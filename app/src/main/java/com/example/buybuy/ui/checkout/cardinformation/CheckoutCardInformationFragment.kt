package com.example.buybuy.ui.checkout.cardinformation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentCheckoutCardinformationBinding
import com.example.buybuy.ui.checkout.CheckOutViewModel

import com.example.buybuy.util.CardNumberTextWatcher
import com.example.buybuy.util.viewBinding
import java.time.LocalDate

class CheckoutCardInformationFragment() : Fragment(R.layout.fragment_checkout_cardinformation) {


    private val binding: FragmentCheckoutCardinformationBinding by viewBinding(
        FragmentCheckoutCardinformationBinding::bind
    )
    private val viewModel: CheckOutViewModel by viewModels({requireParentFragment()})
    private val yearList = createYearList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

    }

    private fun initUi() {
        val list = (1..10).map { String.format("%02d", it) }.toTypedArray()


        binding.spinnerMonth.adapter = ArrayAdapter(
            requireContext(),
            androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, list
        )

        binding.spinnerYear.adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            yearList
        )

        initLastData()
        initTextChangeListeners()

    }

    private fun initLastData() {
        viewModel.lastCardInformationData.cardNumber?.let {
            binding.etCartNumber.setText(it)
        }
        viewModel.lastCardInformationData.cvv?.let {
            binding.etCvc.setText(it)
        }
        viewModel.lastCardInformationData.expirationMonth?.let {
            binding.spinnerMonth.setSelection(it.toInt() - 1)
        }
        viewModel.lastCardInformationData.expirationYear?.let {
            binding.spinnerYear.setSelection(yearList.indexOf(it))
        }
    }

    private fun initTextChangeListeners() {
        binding.etCartNumber.addTextChangedListener(CardNumberTextWatcher(binding.etCartNumber){
            viewModel.lastCardInformationData=viewModel.lastCardInformationData.copy(cardNumber = it)
        })
        binding.etCvc.doOnTextChanged { text, _, _, _ ->
            viewModel.lastCardInformationData=viewModel.lastCardInformationData.copy(cvv = text.toString())
        }


        binding.spinnerYear.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.lastCardInformationData=viewModel.lastCardInformationData.copy(expirationYear = p0?.getItemAtPosition(p2).toString())

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
       binding.spinnerMonth.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
           override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               viewModel.lastCardInformationData=viewModel.lastCardInformationData.copy(expirationMonth = parent.getItemAtPosition(position).toString())
           }

           override fun onNothingSelected(parent: AdapterView<*>) {
           }
       }
    }

    private fun createYearList(): List<String> {
        val year = LocalDate.now().year
        return (year..year + 20).map { it.toString() }

    }

    private fun checkInformation(): Boolean {
        return binding.etCartNumber.text.toString().length >= 16
    }
}