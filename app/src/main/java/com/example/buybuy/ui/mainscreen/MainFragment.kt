package com.example.buybuy.ui.mainscreen

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
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
import com.example.buybuy.databinding.FragmentMainBinding
import com.example.buybuy.domain.model.MainRecycleViewdata
import com.example.buybuy.ui.mainscreen.adapter.MainRecycleViewAdapter
import com.example.buybuy.ui.mainscreen.adapter.ViewPagerAdapter
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment() : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    private val RVadapter: MainRecycleViewAdapter by lazy {
        MainRecycleViewAdapter(this)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initObservers()
        initUi()


    }


    private fun initUi() {
        binding.recyclerView.apply {
            adapter = RVadapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        binding.ivMenu.setOnClickListener {
           ( requireActivity() as MainActivity).openDrawerClick()
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    private fun initRecyclerView(mainRecycleViewdata: List<MainRecycleViewdata>) {
        //RVadapter.submitList(mainRecycleViewdata)
        RVadapter.update(mainRecycleViewdata)
    }

    private fun initObservers() {
        with(viewModel) {
            with(binding) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        vpBannerDataFlow.collect {
                            initRecyclerView(it)
                        }
                    }

                }
            }
        }
    }


}