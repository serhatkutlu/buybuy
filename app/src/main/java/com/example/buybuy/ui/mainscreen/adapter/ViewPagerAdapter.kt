package com.example.buybuy.ui.mainscreen.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.buybuy.ui.mainscreen.productbycategoryFragment.ProductByCategoryFragment
import com.example.buybuy.util.Constant.CATEGORY

class ViewPagerAdapter(fragment: Fragment,private val categories: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ProductByCategoryFragment()
        fragment.arguments = Bundle().apply {
                putString(CATEGORY, categories[position])
        }
        return fragment
    }
}