package com.example.buybuy.ui.mainscreen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()


    }

    private fun initUi() {
    }

    private fun initRecyclerView(mainRecycleViewdata: List<MainRecycleViewdata>) {
        binding.recyclerView.apply {
            adapter = MainRecycleViewAdapter(mainRecycleViewdata, this@MainFragment)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    vpBannerDataFlow.collect {
                        initRecyclerView(it)
                    }
                }
            }
        }
    }


}