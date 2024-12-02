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
import com.example.buybuy.util.invisible
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
                findNavController().popBackStack(R.id.mainFragment, false )
            }
        })
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favoritesState.collect{

                    when(it){
                        is Resource.Success -> {
                            if (it.data.isNullOrEmpty()) {
                             showAnimation()
                            }else{
                               hideAnimation()
                            }

                            binding.includedLayout.progressBar.gone()
                            favoritesAdapter.submitList(it.data)
                        }
                        is Resource.Error -> {
                            showAnimation()
                            favoritesAdapter.submitList(listOf())
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
        initRV()
        with(binding.includedLayout){
            searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) viewModel.getFavorites()
                    else{
                        viewModel.searchOnFavorites(newText )

                    }
                return true}

            })
        }
    }

    private fun showAnimation() {
        binding.includedLayout.root.invisible()
        binding.llLottie.visible()
        binding.lottieAnimationsView.playAnimation()
    }

    private fun hideAnimation() {
        binding.includedLayout.root.visible()
        binding.llLottie.invisible()
        binding.lottieAnimationsView.pauseAnimation()
    }

    private fun initRV() {
        with(binding.includedLayout){
            recyclerView.adapter=favoritesAdapter
            recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            favoritesAdapter.onItemClickedCart=::onItemClicked
            favoritesAdapter.onItemClickedDelete=::onItemClickedDelete

        }

    }

    private fun onItemClickedDelete(productDetailUI: ProductDetailUI) {
        viewModel.deleteFromFavorites(productDetailUI)
    }

    private fun onItemClicked(productDetail: ProductDetailUI) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addToCart(productDetail).collect{
                when(it){
                    is Resource.Success ->  requireContext().showToast(getString(R.string.snackbar_message_cart))
                    is Resource.Error->     requireContext().showToast(getString(R.string.unknown_error))
                    else->{}
                }

            }
        }

    }
}