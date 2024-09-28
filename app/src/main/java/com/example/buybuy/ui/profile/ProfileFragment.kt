package com.example.buybuy.ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentProfileBinding
import com.example.buybuy.ui.profile.adapter.ProfileAdapter
import com.example.buybuy.util.gone
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel by viewModels<ProfileViewModel>()
    private val adapter by lazy { ProfileAdapter(::optionClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnBackPressed()
        initUi()
        initObservers()
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileOptions.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.includeProgressBar.progressBar.visible()
                        }

                        is Resource.Success -> {
                            binding.includeProgressBar.progressBar.gone()
                            adapter.submitList(it.data)
                        }

                        is Resource.Error -> {
                            requireContext().showToast(it.message)

                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun initUi() {
        initToolBar()
        initRV()

    }

    private fun initToolBar() {


        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val alpha = (totalScrollRange + verticalOffset).toFloat() / totalScrollRange
            binding.includedProfile.linearLayout.alpha = alpha

            if (verticalOffset != -appBarLayout.totalScrollRange) {
                binding.tvTitle.setTextColor(Color.WHITE)
            } else {

                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                )


            }
        }
    }

    private fun initRV() {
        binding.rvProfile.adapter = adapter
        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun optionClickListener(option:Int){
        when(option){
            2->{
                findNavController().navigate(R.id.action_profileFragment_to_addressFragment,null,NavOptions.navOption1)
            }
            else->{}
        }
    }
}