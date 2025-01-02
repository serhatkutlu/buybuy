package com.example.buybuy.ui.mainscreen

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.ui.MainActivity
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentMainBinding
import com.example.buybuy.domain.model.data.ProductDetailUI
import com.example.buybuy.domain.model.sealed.MainRecycleViewTypes
import com.example.buybuy.ui.mainscreen.adapter.MainRecycleViewAdapter
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.gone
import com.example.buybuy.util.invisible
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.viewBinding
import com.example.buybuy.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CATEGORY_STATE="category_state"
private const val FLASH_SALE_STATE="flash_sale_state"
private const val MAIN_RV_STATE="main_rv_state"

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private var isfirstinit = false


    private var state:Parcelable?=null
    private val rvAdapter: MainRecycleViewAdapter by lazy {
        MainRecycleViewAdapter(
            contentClickListener = ::openProductDetailScreen,
            favoriteClickListener = ::addToFavorite,
            fetchContentData = ::fetchContentDataForCategoryViewHolder,
            viewModel.currentCategory
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isfirstinit = true


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchMainContent()
        }


        handleOnBackPressed()
        initObservers()
        initUi()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val categoryState = savedInstanceState?.getParcelable<Parcelable>(CATEGORY_STATE)
        val flashSaleState = savedInstanceState?.getParcelable<Parcelable>(FLASH_SALE_STATE)
        val innerState=Pair(categoryState,flashSaleState)
        if (innerState.first!=null || innerState.second!=null){
            rvAdapter.updateState(innerState)
        }

        state=savedInstanceState?.getParcelable(MAIN_RV_STATE)
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if ((requireActivity() as MainActivity).binding.drawerLayout.isDrawerOpen(
                            GravityCompat.START
                        )
                    ) {
                        (requireActivity() as MainActivity).binding.drawerLayout.closeDrawer(
                            GravityCompat.START
                        )
                    } else {
                        requireContext().showAlertDialog(
                            getString(R.string.alert_title_exit),
                            getString(R.string.alert_message_exit),
                            positiveButtonAction = {
                                requireActivity().finish()
                            })
                    }
                }
            })
    }

    private fun initUi() {

        initRV()
        binding.ivMenu.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerClick()
        }
        binding.cvSearch.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_searchFragment,
                null,
                NavOptions.upAnim
            )
        }
        binding.includedError.buttonRefresh.setOnClickListener {
            viewModel.fetchMainContent()
        }
        binding.swipeRefreshLayout.setOnRefreshListener{
            viewModel.fetchMainContent()
            binding.swipeRefreshLayout.isRefreshing = false
            rvAdapter.notifyDataSetChanged()
        }
    }

    private fun initRV() {
        binding.recyclerView.apply {
            adapter = rvAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

    }

    private fun addToFavorite(
        productDetail: ProductDetailUI,
        position: Int,
        type: MainRecycleViewTypes
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.addToFavorite(productDetail)) {
                val isFavorite = !productDetail.isFavorite
                rvAdapter.updateSingleProductItem(
                    productDetail.copy(isFavorite = isFavorite),
                    position,
                    type
                )
                viewModel.fetchContentForCategory().collect {}
            }

        }

    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isAdded && view != null && binding.recyclerView.layoutManager != null) {
            val innerState = rvAdapter.saveState()
            val state = binding.recyclerView.layoutManager?.onSaveInstanceState()
            outState.putParcelable(CATEGORY_STATE, innerState.first)
            outState.putParcelable(FLASH_SALE_STATE, innerState.second)
            outState.putParcelable(MAIN_RV_STATE, state)
        }


    }

    override fun onStop() {
        super.onStop()
        rvAdapter.saveState()
    }
    private fun initObservers() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainRvData.collect { response ->
                        when (response) {
                            is Resource.Success -> {
                                val isError = response.data?.any {
                                    if (it is MainRecycleViewTypes.RVCategory) {
                                        it.data is Resource.Error

                                    } else false
                                }
                                if (!isError!!) {
                                    binding.includedError.root.gone()
                                    binding.recyclerView.visible()
                                    binding.progressBar.gone()
                                    rvAdapter.submitList(response.data){
                                        binding.recyclerView.layoutManager?.onRestoreInstanceState(state)

                                    }
                                    if (isfirstinit) {
                                        val indexCategory =
                                            response.data.indexOfFirst { it is MainRecycleViewTypes.RVCategory }
                                        val indexFlashSale =
                                            response.data.indexOfFirst { it is MainRecycleViewTypes.FlashSaleDataUi }
                                        isfirstinit = false
                                        rvAdapter.updateRvItems(
                                            listOf(
                                                response.data[indexCategory],
                                                response.data[indexFlashSale]
                                            )
                                        )
                                    }

                                } else {
                                    binding.recyclerView.invisible()
                                    binding.progressBar.gone()
                                    binding.includedError.root.visible()
                                }


                            }

                            is Resource.Loading->{
                                binding.includedError.root.gone()
                                binding.recyclerView.visible()
                                binding.progressBar.visible()

                            }

                            else->{}
                        }
                    }

                }
            }

        }
    }

    private fun fetchContentDataForCategoryViewHolder(
        category: String,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchContentForCategory(category).collect {
                    it?.let {
                        rvAdapter.updateRvItems(listOf(it))
                    }

                }
            }
        }
    }




    private fun openProductDetailScreen(product: ProductDetailUI) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToProductDetailFragment(
                product
            ), navOptions = NavOptions.rightAnim
        )
    }


}