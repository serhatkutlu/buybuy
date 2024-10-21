package com.example.buybuy.ui.address.addressScreen

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
import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.databinding.FragmentAddressBinding
import com.example.buybuy.enums.ToastType
import com.example.buybuy.ui.address.addressScreen.adapter.AddressRvAdapter
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressFragment : Fragment(R.layout.fragment_address) {

    private val binding: FragmentAddressBinding by viewBinding(FragmentAddressBinding::bind)
    private val viewModel: AddressViewModel by viewModels()
    private val adapter: AddressRvAdapter by lazy { AddressRvAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllAddress()
    }
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllAddressResponse.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.includeProgressBar.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.includeProgressBar.progressBar.visibility = View.GONE
                            adapter.updateList(it.data ?: listOf())

                        }

                        is Resource.Error -> {
                            binding.includeProgressBar.progressBar.visibility = View.GONE
                            ToastType.ERROR.showToast(requireContext())
                        }

                        else -> Unit
                    }
                }
            }
        }

    }

    private fun initUi() {

        initRV()

        with(binding) {
            tvNew.setOnClickListener {
                findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToNewAddressFragment(AddressData()), navOptions = NavOptions.navOptions1
            )
            }
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initRV() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onClick = ::onItemClick

    }

    private fun onItemClick(address: AddressData?) {

        findNavController().navigate(
           AddressFragmentDirections.actionAddressFragmentToNewAddressFragment(address), navOptions = NavOptions.navOptions1
        )

    }
}
