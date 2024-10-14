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
import com.example.buybuy.databinding.FragmentProductsByCategoryBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.ui.mainscreen.MainFragmentDirections
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.adapter.ProductByCategoryAdapter
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.Constant.CATEGORY
import com.example.buybuy.util.Constant.DECORATION_SPACE
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
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
        ProductByCategoryAdapter()
    }

     var category:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle?.let {
            category = bundle.getString(CATEGORY)
            category?.let {
                viewModel.getProductByCategories(it)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        with(binding) {


            recyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter.onClickListener = ::beginNavigate
            adapter.onFavoriteClickListener = ::addToFavorite
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                SpacesItemDecoration(
                    spaceleft = DECORATION_SPACE,
                    spacetop = DECORATION_SPACE

                )
            )
            //loadLastRVPosition()
        }
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
                                    progressBar.gone()
                                    it.data?.let {


                                        adapter.submitList(it)
                                        loadLastRVPosition()



                                    }
                                }

                                is Resource.Error -> {
                                    requireContext().showToast(it.message)
                                }
                                is Resource.Empty -> {}
                            }

                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveLastRVPosition()

        parentFragmentManager.beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }

    private fun beginNavigate(product: ProductDetailUI) {
        //findNavController().navigate(MainFragmentDirections.actionMainFragmentToProductDetailFragment(product))
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToProductDetailFragment(
                product
            )
        )
    }

    private fun addToFavorite(product: ProductDetailUI) {
        viewModel.addToFavorite(product)
    }


    private fun saveLastRVPosition() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
        position
        requireContext().sharedPreferences.edit().putInt(category, position).apply()
    }

    private fun loadLastRVPosition() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        val position = getLastRVPosition()
        layoutManager.scrollToPosition(position)
        position

    }

    private fun getLastRVPosition(): Int {

        return requireContext().sharedPreferences.getInt(category, 0)
    }
}