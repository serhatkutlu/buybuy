package com.example.buybuy.ui.mainscreen.productbycategoryFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentProductsByCategoryBinding
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter.ProductByCategoryAdapter
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter.SpacesItemDecoration
import com.example.buybuy.util.Constant.CATEGORY
import com.example.buybuy.util.Constant.DECORATION_SPACE
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductByCategoryFragment() : Fragment(R.layout.fragment_products_by_category) {

    private val binding by viewBinding(FragmentProductsByCategoryBinding::bind)
    private val viewModel: ProductByCategoryViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        bundle?.let {
            val category = bundle.getString(CATEGORY)
            category?.let {
                viewModel.getProductByCategories(category)

            }
        }

        initObservers()
    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.productByCategories.collect {
                            when (it) {
                                is Resource.Loading -> progressBar.Visible()
                                is Resource.Success -> {
                                    progressBar.Gone()
                                    it.data?.let {
                                        recyclerView.addItemDecoration(
                                            SpacesItemDecoration(
                                                DECORATION_SPACE
                                            )
                                        )
                                        recyclerView.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                        recyclerView.adapter = ProductByCategoryAdapter(it)

                                    }
                                }

                                is Resource.Error -> {
                                    requireContext().showToast(it.message)
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}