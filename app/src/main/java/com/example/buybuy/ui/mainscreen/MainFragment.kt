package com.example.buybuy.ui.mainscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.MainActivity
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.FragmentMainBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.adapter.MainRecycleViewAdapter
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment() : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    private val RVadapter: MainRecycleViewAdapter by lazy {
        MainRecycleViewAdapter()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initObservers()
        initUi()


    }


    private fun initUi() {

        initRV()
        binding.ivMenu.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerClick()
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    private fun initRV() {
        binding.recyclerView.apply {
            adapter = RVadapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        RVadapter.fetchContentData = ::fetchContentDataForRecycleView
        RVadapter.contentClickListener =::openProductDetailScreen
        RVadapter.contentFavoriteClickListener=::addToFavorite
    }

    private fun addToFavorite(productDetail: ProductDetail) {
            viewModel.addToFavorite(productDetail)
    }

    override fun onStop() {
        super.onStop()

        RVadapter.saveState()

    }

    private fun UpdateVpBannerData(mainRecycleViewdata: List<MainRecycleViewdata>) {
        RVadapter.submitList(mainRecycleViewdata)


    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        vpBannerDataFlow.collect {
                            UpdateVpBannerData(it)
                        }
                    }

                }
            }
        }
    }

    fun fetchContentDataForRecycleView(category: String) =
        viewModel.fetchContentForCategory(category)

    fun openProductDetailScreen(product: ProductDetail) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToProductDetailFragment(
                product
            )
        )
    }

}