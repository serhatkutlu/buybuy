package com.example.buybuy.ui.mainscreen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.data.model.data.VpBannerData
import com.example.buybuy.databinding.FragmentMainBinding
import com.example.buybuy.ui.mainscreen.recycleview.MainRecycleViewAdapter
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment() : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainRecycleViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initUi()


    }

    private fun initUi() {
    }

    private fun initRecylerView(VpBannerData: List<VpBannerData>) {
        binding.recyclerView.apply {
            adapter = MainRecycleViewAdapter(VpBannerData)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    vpBannerDataFlow.collect {
                        when (it) {
                            is Resource.Loading -> {
                                progressBar.Visible()
                            }

                            is Resource.Success -> {
                                progressBar.Gone()
                                Log.d("daddd",it.data?.get(0)?.type.toString())
                                initRecylerView(it.data!!)
                            }
                            is Resource.Error -> {
                                progressBar.Gone()
                                requireContext().showToast(it.message)
                            }
                        }
                    }
                }
            }
        }
    }


}