package com.example.buybuy.ui

import android.os.Bundle
import android.view.MenuItem

import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.buybuy.R
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.databinding.ActivityMainBinding
import com.example.buybuy.databinding.NavHeaderBinding
import com.example.buybuy.util.NavOptions
import com.example.buybuy.util.gone
import com.example.buybuy.util.Resource
import com.example.buybuy.util.visible
import com.example.buybuy.util.setImage
import com.example.buybuy.util.showAlertDialog
import com.example.buybuy.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private val viewmodel: ActivityViewModel by viewModels()


    private lateinit var headerBinding: NavHeaderBinding

    @Inject
    lateinit var fakeApi: FakeStoreApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        headerBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))



        initLogOutObserver()
        initNavHeaderObserver()
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.closed)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.loginFragment,
                R.id.signupFragment,
                R.id.forgotPasswordFragment,
                R.id.productDetailFragment,
                R.id.splashFragment,
                R.id.addressFragment,
                R.id.checkoutFragment,
                R.id.orderSuccessful,
                R.id.myOrdersFragment,
                R.id.newAddressFragment -> {
                    binding.bottomNavigation.gone()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


                }

                else -> {
                    if (destination.id == R.id.mainFragment) {
                        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    } else {
                        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    }
                    binding.bottomNavigation.visible()
                    if (viewmodel.user.value == Resource.Empty) {
                        viewmodel.getUserData()
                    }
                }
            }
        }




        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_orders -> {
                    navController.navigate(R.id.myOrdersFragment, null, NavOptions.rightAnim)
                    true
                }

                R.id.nav_logout -> {
                    showAlertDialog(
                        getString(R.string.fragment_profile_alert_title),
                        getString(R.string.fragment_profile_alert_message),
                        positiveButtonAction = {
                            viewmodel.logOut()
                        })


                    true
                }

                R.id.nav_profile -> {
                    navController.navigate(R.id.action_mainFragment_to_profileFragment)
                    true
                }
                R.id.nav_coupons -> {
                    navController.navigate(R.id.couponsFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainFragment -> {
                    navController.popBackStack(R.id.mainFragment, false)
                    true
                }

                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment, null, NavOptions.rightAnim)
                    true
                }

                R.id.favoritesFragment -> {
                    val navOptions =if (navController.currentDestination?.id != R.id.mainFragment) NavOptions.leftAnim else NavOptions.rightAnim
                    navController.navigate(R.id.favoritesFragment, null, navOptions)
                    true
                }

                R.id.cartFragment -> {
                    val navOptions =if (navController.currentDestination?.id != R.id.profileFragment) NavOptions.rightAnim else NavOptions.leftAnim
                    navController.navigate(R.id.cartFragment, null, navOptions)
                    true
                }

                else -> {
                    false
                }

            }
        }

    }

    private fun initLogOutObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.logOutState.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.progressBar.gone()
                            navController.navigate(
                                R.id.splashFragment,
                                null,
                                NavOptions.navOptions4
                            )

                        }

                        is Resource.Loading -> binding.progressBar.visible()
                        is Resource.Error -> {
                            binding.progressBar.gone()
                            showToast(it.message)
                        }

                        is Resource.Empty -> {}
                    }
                }


            }
        }

    }

    private fun initNavHeaderObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.user.collect {
                    when (it) {
                        is Resource.Success -> {
                            headerBinding.navHeaderImage.setImage(it.data?.image ?: "")
                            headerBinding.navHeaderUsername.text = it.data?.name ?: ""
                            headerBinding.navHeaderEmail.text = it.data?.email ?: ""
                        }

                        is Resource.Error -> {
                            showToast(it.message)
                        }

                        is Resource.Loading -> {}
                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDrawerClick() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}