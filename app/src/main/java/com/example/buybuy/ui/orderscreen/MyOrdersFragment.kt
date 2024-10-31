package com.example.buybuy.ui.orderscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentMyOrdersBinding
import com.example.buybuy.databinding.FragmentOrderSuccessfulBinding
import com.example.buybuy.enums.ToastMessage
import com.example.buybuy.ui.orderscreen.adapter.MyOrdersAdapter
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {

    private val binding by viewBinding(FragmentMyOrdersBinding::bind)
    private val viewModel: MyOrdersViewModel by viewModels()
    private  val adapter:MyOrdersAdapter by lazy{
        MyOrdersAdapter(listOf())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.orders.collect{
                    when(it){
                        is Resource.Success->{
                            binding.progressBar.visibility=View.GONE
                            adapter.updateList(it.data?: listOf())
                        }

                        is Resource.Loading->{
                            binding.progressBar.visibility=View.VISIBLE
                        }
                        is Resource.Error->{
                            binding.progressBar.visibility=View.GONE
                            ToastMessage.ERROR.showToast(requireContext())
                        }
                        else->{
                            binding.progressBar.visibility=View.GONE

                        }

                    }
                }
            }

        }
    }

    private fun initUi() {
        initRv()
    }

    private fun initRv() {
        binding.recyclerView.adapter=adapter
    }


}