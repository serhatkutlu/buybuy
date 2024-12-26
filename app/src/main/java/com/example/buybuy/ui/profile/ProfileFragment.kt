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
import com.example.buybuy.enums.ProfileOptionsEnum
import com.example.buybuy.ui.MainActivity
import com.example.buybuy.ui.profile.adapter.ProfileAdapter
import com.example.buybuy.util.gone
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.Resource
import com.example.buybuy.util.invisible
import com.example.buybuy.util.setImage
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.visible
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel by viewModels<ProfileViewModel>()
    private val adapter by lazy { ProfileAdapter(::optionClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnBackPressed()
        initUi()
        initObservers()
        initLogOutObserver()
    }


    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(
                        R.id.mainFragment,
                        null,
                        NavOptions.navOptions3.build()
                    )
                }
            })
    }

    private fun initObservers() {

        collectProfileOptions()

        collectUserData()


    }

    private fun collectUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.includeProgressBar.progressBar.invisible()
                            binding.includedProfile.ivProfile.setImage(it.data?.image ?: "")
                            binding.includedProfile.tvName.text = it.data?.name
                            binding.includedProfile.tvMail.text = it.data?.email
                        }

                        is Resource.Loading -> {
                            binding.includeProgressBar.progressBar.visible()
                        }

                        is Resource.Error -> {
                            binding.includeProgressBar.progressBar.invisible()
                            requireContext().showToast(it.message)
                        }

                        else -> {
                            binding.includeProgressBar.progressBar.invisible()
                        }
                    }
                }
            }
        }

    }

    private fun collectProfileOptions() {
        viewLifecycleOwner.lifecycleScope.launch {
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

    private fun initUi() {
        initToolBar()
        initRV()

    }


    private fun initLogOutObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logOutState.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.includeProgressBar.progressBar.gone()
                            findNavController().navigate(
                                R.id.splashFragment,
                                null,
                                NavOptions.navOptions4
                            )

                        }

                        is Resource.Loading -> binding.includeProgressBar.progressBar.visible()
                        is Resource.Error -> {
                            binding.includeProgressBar
                                .progressBar.gone()
                            requireContext().showToast(it.message)
                        }

                        is Resource.Empty -> {}
                    }
                }


            }
        }

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

    private fun optionClickListener(type: ProfileOptionsEnum) {
        when (type) {
            ProfileOptionsEnum.ORDER -> {
                findNavController().navigate(R.id.myOrdersFragment, null, NavOptions.rightAnim)
            }

            ProfileOptionsEnum.COUPONS -> {
                findNavController().navigate(
                    R.id.action_profileFragment_to_couponsFragment,
                    null,
                    NavOptions.rightAnim
                )
            }
//            ProfileOptionsEnum.PAYMENT->{
//                findNavController().navigate(R.id.action_profileFragment_to_paymentFragment,null,NavOptions.navOption1)
//            }
//            ProfileOptionsEnum.SETTINGS->{
//                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment,null,NavOptions.navOption1)
//            }
//            ProfileOptionsEnum.HELP->{
//                findNavController().navigate(R.id.action_profileFragment_to_helpFragment,null,NavOptions.navOption1)
//            }
//            ProfileOptionsEnum.ABOUT->{
//                findNavController().navigate(R.id.action_profileFragment_to_aboutFragment,null,NavOptions.navOption1)
//            }
            ProfileOptionsEnum.LOGOUT -> {
                requireContext().showAlertDialog(
                    getString(R.string.fragment_profile_alert_title),
                    getString(R.string.fragment_profile_alert_message),
                    positiveButtonAction = {
                        (requireActivity() as MainActivity).logOut()
                    })


            }

            ProfileOptionsEnum.ADDRESS -> {
                findNavController().navigate(
                    R.id.action_profileFragment_to_addressFragment,
                    null,
                    NavOptions.rightAnim
                )
            }

            else -> {}

        }
    }
}