package com.example.buybuy.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.databinding.FragmentProductDetailScreenBinding
import com.example.buybuy.ui.productdetail.adapter.ProductDetailAdapter
import com.example.buybuy.util.Constant.CURRENCYSYMBOL
import com.example.buybuy.util.Constant.POPULAR
import com.example.buybuy.util.Gone
import com.example.buybuy.util.Invisible
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.Visible
import com.example.buybuy.util.generateDiscount
import com.example.buybuy.util.setImage
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail_screen) {

    private val binding by viewBinding(FragmentProductDetailScreenBinding::bind)
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()
    private val tabs: MutableList<String> = mutableListOf()
    private var isDetailopen = false
    private var isRotated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTabsList()
        initUi()
        initObservers()

    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isFavorite.collect{
                    if (it){
                        binding.includedLayout.cvFavorite.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.orange))
                    }else binding.includedLayout.cvFavorite.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))

                }
            }
        }
    }

    private fun createTabsList() {
        with(args.product) {
            tabs.apply {
                add(model)
                if (color!=null) add(color)
                add(category)
                if (popular) add(POPULAR)
            }

        }

    }

    private fun initUi() {
        with(binding) {
            with(args.product) {
                with(viewModel) {



                    imageView.setImage(image)
                    rating.rating = star ?: 0.0f
                    tvRating.text = star.toString()
                    includedLayout.cvFavorite.setOnClickListener {
                    }
                    recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = ProductDetailAdapter(tabs)
                    recyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 40))
                    tvTitle.text = title
                    tvDescription.text = description
                    imDetatilButton.setOnClickListener {
                        initDetailButton()
                    }
                    var newprice = price.toString()
                    if (discount != 0) {
                        tvPriceOld.Visible()
                         newprice = (price generateDiscount discount).toString()
                        tvPrice.text = (newprice+CURRENCYSYMBOL)
                        tvPriceOld.text=(price.toString()+CURRENCYSYMBOL)
                        tvPriceOld.paint.isStrikeThruText=true
                    }
                    tvPriceNew.text =(newprice+CURRENCYSYMBOL)


                }
            }

        }
    }

    private fun initDetailButton() {
        when (isDetailopen) {
            false -> {
                isDetailopen = true

                startAnimation(10)
            }

            else-> {
                isDetailopen = false
                startAnimation(3)
            }
        }
    }



    private fun startAnimation(maxLines: Int) {


        val targetRotation = if (isRotated) 0f else 180f
        val rotateAnimation = RotateAnimation(
            if (isRotated) 180f else 0f, targetRotation,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 300
        rotateAnimation.repeatCount = 0 // Her basışta bir kez dönmesi için
        rotateAnimation.fillAfter = true

        rotateAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.tvDescription.maxLines = maxLines
                binding.tvTitle.maxLines=maxLines/3
                isRotated = !isRotated // Döndürme durumunu güncelle
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        binding.imDetatilButton.startAnimation(rotateAnimation)
    }
}