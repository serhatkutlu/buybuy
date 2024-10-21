package com.example.buybuy.ui.favoritesscreen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentFavoritesBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.ui.favoritesscreen.adapter.FavoritesAdapter
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoritesFragment: Fragment(R.layout.fragment_favorites) {

    private val binding:FragmentFavoritesBinding by viewBinding(FragmentFavoritesBinding::bind)
    private val viewModel:FavoritesViewModel by viewModels()
    private val favoritesAdapter: FavoritesAdapter by lazy { FavoritesAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initObservers()
        handleOnBackPressed()


    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.mainFragment)
            }
        })
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favoritesState.collect{

                    when(it){
                        is Resource.Success -> {
                            binding.includedLayout.progressBar.gone()
                            favoritesAdapter.submitList(it.data)
                        }
                        is Resource.Error -> {
                            binding.includedLayout.progressBar.gone()
                            requireContext().showToast(it.message)
                        }
                        is Resource.Loading -> {
                            binding.includedLayout.progressBar.visible()
                        }else->{}
                    }

                }
            }
        }
    }

    private fun initUi() {
        with(binding.includedLayout){
            initRV()
            searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchOnFavorites(query ?:"")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                return true}

            })
        }
    }

    private fun initRV() {
        with(binding.includedLayout){
            recyclerView.adapter=favoritesAdapter
            recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            favoritesAdapter.onItemClicked=::onItemClicked
        }

    }

    private fun onItemClicked(productDetail: ProductDetailUI) {
        viewModel.addToCart(productDetail)
        requireContext().showToast(getString(R.string.snackbar_message_cart))
    }
}