package com.example.buybuy.ui.mainscreen

import android.os.Bundle
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

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    private val rvAdapter: MainRecycleViewAdapter by lazy {
        MainRecycleViewAdapter(
            contentClickListener = ::openProductDetailScreen,
            favoriteClickListener = ::addToFavorite,
            fetchContentData = ::fetchContentDataForCategoryViewHolder
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handleOnBackPressed()
        initObservers()
        initUi()


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
        binding.includedError.buttonRefresh.setOnClickListener{
            viewModel.fetchMainContent()
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

            }

        }

    }

    override fun onStop() {
        super.onStop()

        rvAdapter.saveState()

    }

    private fun updateMainRvList(mainRecycleViewData: List<MainRecycleViewTypes>) {
        rvAdapter.submitList(mainRecycleViewData)
    }

    private fun initObservers() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainRvData.collect {
                        val isError=it.any {
                            if (it is MainRecycleViewTypes.RVCategory) {
                                it.data is Resource.Error
                            } else false
                        }
                        if (!isError) {
                            binding.includedError.root.gone()
                            binding.recyclerView.visible()
                            updateMainRvList(it)
                        } else {
                            binding.recyclerView.invisible()
                            binding.includedError.root.visible()
                        }
                    }
                }

            }

        }
    }

    private fun fetchContentDataForCategoryViewHolder(
        category: String,
        isFavoriteClick: Boolean = false
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchContentForCategory(category, isFavoriteClick).collect {
                    it?.let {
                        rvAdapter.updateCategoryItem(it)
                    }

                }
            }
        }
    }

//    private fun fetchContentDataForFlashSaleViewHolder() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                val response = viewModel.getFlashSaleList()
//                response?.let {
//                    rvAdapter.updateCategoryItem(it)
//                }
//            }
//        }
//    }

    private fun openProductDetailScreen(product: ProductDetailUI) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToProductDetailFragment(
                product
            ), navOptions = NavOptions.rightAnim
        )
    }

}