package com.example.buybuy.ui.searchscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentSearchBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.ui.searchscreen.adapters.SearchScreenAdapter
import com.example.buybuy.util.gone
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewmodel: SearchFragmentViewModel by viewModels()
    val inputMethodManager : InputMethodManager by lazy{
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val rvAdapter by lazy {
        SearchScreenAdapter()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservers()

    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.searchData.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.includedLayout.progressBar.visible()
                        }

                        is Resource.Success -> {
                            binding.includedLayout.progressBar.gone()
                            rvAdapter.submitList(it.data)
                        }

                        is Resource.Error -> {
                            binding.includedLayout.progressBar.gone()
                            requireContext().showToast(it.message)
                        }
                        is Resource.Empty -> {}
                    }
                }
            }
        }
    }

    private fun initUi() {
        with(binding.includedLayout) {

            initRecyclerView()
            ivBack.visible()
            ivBack.setOnClickListener {
                    handleBackButton()
                }
            searchView.setOnClickListener {
                searchView.isIconified = false
            }


            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener  {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    viewmodel.performSearch(p0.toString())

                    inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }

            })

        }
    }

    private fun initRecyclerView() {
        with(binding.includedLayout.recyclerView){
            adapter=rvAdapter
            layoutManager= GridLayoutManager(requireContext(),2)
            addItemDecoration(SpacesItemDecoration(25, spaceleft = 30, spaceright = 30))
            rvAdapter.onFavoriteClickListener=::onFavoriteClick
            rvAdapter.onClickListener=::onItemClick
        }
    }

    private fun onFavoriteClick(product: ProductDetailUI){
        viewmodel.addToFavorite(product)
    }
    private fun onItemClick(product:ProductDetailUI){
        val action=SearchFragmentDirections.actionSearchFragmentToProductDetailFragment(product)
        findNavController().navigate(action,NavOptions.rightAnim)
    }

    private fun handleBackButton() {
            findNavController().navigateUp()

    }
}