package com.example.buybuy.ui.orderscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.buybuy.R
import com.example.buybuy.databinding.DialogRatingBarBinding
import com.example.buybuy.databinding.FragmentMyOrdersBinding
import com.example.buybuy.databinding.FragmentOrderSuccessfulBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.enums.ToastMessage
import com.example.buybuy.ui.orderscreen.adapter.MyOrdersAdapter
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.gone
import com.example.buybuy.util.setRatingText
import com.example.buybuy.util.viewBinding
import com.example.buybuy.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {

    private val binding by viewBinding(FragmentMyOrdersBinding::bind)
    private val viewModel: MyOrdersViewModel by viewModels()
    private  val adapter:MyOrdersAdapter by lazy{
        MyOrdersAdapter(listOf(),::clickListener)
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
                            if (!it.data.isNullOrEmpty()){
                                binding.recyclerView.visible()
                                binding.llLottie.gone()
                                adapter.updateList(it.data)
                            }
                            else{
                                binding.recyclerView.gone()
                                binding.llLottie.visible()
                            }
                            binding.progressBar.visibility=View.GONE

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
        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun initRv() {
        binding.recyclerView.adapter=adapter
    }


    private fun clickListener(product: ProductDetailUI){
        showDialog()
    }

    private fun showDialog() {
        val dialogView=DialogRatingBarBinding.inflate(layoutInflater)
        val dialog= AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
                .setTitle(getText(R.string.fragment_my_orders_alert_title))
            .setCancelable(false)
            .setPositiveButton(getText(R.string.fragment_my_orders_alert_positive_text)){_,_->

                ToastMessage.SUCCESS.showToast(requireContext())
            }

            .create()

        dialog.show()
        dialogView.ratingBar.setOnRatingBarChangeListener{
            _,_,_->
            val ratingText=dialogView.ratingBar.setRatingText(requireContext())
            dialogView.tvRatingText.visibility=View.VISIBLE
            dialogView.tvRatingText.text=ratingText

        }
    }

}