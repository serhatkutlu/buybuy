package com.example.buybuy.ui.coupons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.databinding.FragmentCouponsBinding
import com.example.buybuy.enums.ToastMessage
import com.example.buybuy.ui.coupons.adapter.CouponsAdapter
import com.example.buybuy.util.Resource
import com.example.buybuy.util.gone
import com.example.buybuy.util.viewBinding
import com.example.buybuy.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CouponsFragment : Fragment(com.example.buybuy.R.layout.fragment_coupons) {

    private val binding: FragmentCouponsBinding by viewBinding(FragmentCouponsBinding::bind)
    private val viewModel: CouponsViewModel by viewModels()
    private val adapter: CouponsAdapter by lazy { CouponsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()
    }

    private fun initUi() {
        initRv()
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRv() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coupon.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.progressBar2.gone()
                            adapter.submitList(it.data)

                        }

                        is Resource.Error -> {
                            ToastMessage.ERROR.showToast(requireContext(), it.message)
                        }

                        is Resource.Loading -> {
                            binding.progressBar2.visible()

                        }

                        is Resource.Empty -> {}
                    }
                }
            }
        }
    }
}