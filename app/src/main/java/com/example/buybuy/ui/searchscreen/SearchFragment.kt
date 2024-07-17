package com.example.buybuy.ui.searchscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentSearchBinding
import com.example.buybuy.ui.searchscreen.adapters.SearchScreenAdapter
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.Visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewmodel: SearchFragmentViewModel by viewModels()
    private val RVadapter by lazy {
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
                            binding.progressBar.Visible()
                        }

                        is Resource.Success -> {
                            binding.progressBar.Gone()
                            RVadapter.submitList(it.data)
                        }

                        is Resource.Error -> {
                            binding.progressBar.Gone()
                            requireContext().showToast(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun initUi() {
        with(binding) {

            initRecyclerView()
            ivBack.setOnClickListener {
                    handleBackButton()
                }
            searchView.setOnClickListener {
                searchView.isIconified = false
            }

            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener  {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    viewmodel.PerformSearch(p0.toString())
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }

            })

        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerView){
            adapter=RVadapter
            layoutManager= GridLayoutManager(requireContext(),2)
            addItemDecoration(SpacesItemDecoration(25))
        }
    }

    private fun handleBackButton() {

            findNavController().popBackStack()


    }
}