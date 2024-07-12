package com.example.buybuy.ui.mainscreen.productbycategoryFragment

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
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.FragmentProductsByCategoryBinding
import com.example.buybuy.ui.mainscreen.MainFragmentDirections
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter.ProductByCategoryAdapter
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.Constant.CATEGORY
import com.example.buybuy.util.Constant.DECORATION_SPACE
import com.example.buybuy.util.Constant.lASTRVPOS
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Visible
import com.example.buybuy.util.sharedPreferences
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductByCategoryFragment() : Fragment(R.layout.fragment_products_by_category) {

    private val binding by viewBinding(FragmentProductsByCategoryBinding::bind)
    private val viewModel: ProductByCategoryViewModel by viewModels()
    private val adapter by lazy {
        ProductByCategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle?.let {
            val category = bundle.getString(CATEGORY)
            category?.let {
                viewModel.getProductByCategories(category)

            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initObservers()
    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.productByCategories.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    //progressBar.Visible()
                                }
                                is Resource.Success -> {
                                    progressBar.Gone()
                                    it.data?.let {
                                        recyclerView.addItemDecoration(
                                            SpacesItemDecoration(
                                                spaceleft = DECORATION_SPACE,
                                                spacetop = DECORATION_SPACE

                                            )
                                        )
                                        recyclerView.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )

                                        adapter.submitList(it)
                                        adapter.onClickListener=::beginNavigate
                                        recyclerView.adapter = adapter
                                        loadLastRVPosition()


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
    private fun beginNavigate(product:ProductDetail){
        //findNavController().navigate(MainFragmentDirections.actionMainFragmentToProductDetailFragment(product))
        saveLastRVPosition()
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToProductDetailFragment(product))
    }

    private fun saveLastRVPosition() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        requireContext().sharedPreferences.edit().putInt(lASTRVPOS, position).apply()
    }
    private fun loadLastRVPosition() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPosition(getLastRVPosition())
    }

    private fun getLastRVPosition(): Int {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        layoutManager.findFirstVisibleItemPosition()
       return requireContext().sharedPreferences.getInt(lASTRVPOS,0)
    }
}