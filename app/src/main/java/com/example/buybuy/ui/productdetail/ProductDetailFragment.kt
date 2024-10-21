package com.example.buybuy.ui.productdetail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buybuy.R
import com.example.buybuy.databinding.FragmentProductDetailScreenBinding
import com.example.buybuy.ui.productdetail.adapter.ProductDetailAdapter
import com.example.buybuy.util.Constant.DETAIL_CARD_MAX_HEIGHT
import com.example.buybuy.util.Constant.DETAIL_CARD_MIN_HEIGHT
import com.example.buybuy.util.Constant.POPULAR
import com.example.buybuy.util.SpacesItemDecoration
import com.example.buybuy.util.visible
import com.example.buybuy.util.calculateDiscount
import com.example.buybuy.util.setImage
import com.example.buybuy.util.showToast
import com.example.buybuy.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail_screen) {

    private val binding by viewBinding(FragmentProductDetailScreenBinding::bind)
    private val viewModel: ProductDetailViewModel by viewModels()
    private val  args: ProductDetailFragmentArgs by navArgs()
    private val tabs: MutableMap<String, String> = mutableMapOf()
    private var isExpanded = false
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavoriteFlow.collect {
                    it?.let {
                        args.product.isFavorite = it
                    }

                }
            }
        }
    }

    private fun createTabsList() {
        with(args.product) {
            tabs.apply {


                    put(::color.name, color)



                    put(::category.name, category)

                if (popular) put(::popular.name.lowercase(), POPULAR)
            }

        }

    }

    private fun initUi() {


        with(binding) {
            with(args.product) {
                imageView.setImage(image)
                rating.rating = star
                tvRating.text = star.toString()
                setFavoriteBackground(isFavorite)
                includedLayout.cvFavorite.cardElevation = 30F
                includedLayout.cvFavorite.setOnClickListener {
                    viewModel.addToFavorite(this)
                    setFavoriteBackground(!isFavorite)

                }
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = ProductDetailAdapter(tabs)
                recyclerView.addItemDecoration(SpacesItemDecoration(spaceleft = 40))
                tvTitle.text = title
                tvDescription.text = description
                imDetatilButton.setOnClickListener {
                    initDetailButton()
                }
                var newprice = price.toString()
                val discount = discount
                if (discount != 0) {
                    tvPriceOld.visible()
                    newprice = (price calculateDiscount discount).toString()
                    tvPrice.text = getString(R.string.currency_symbol, newprice)
                    tvPriceOld.text = getString(R.string.currency_symbol, price.toString())
                    tvPriceOld.paint.isStrikeThruText = true
                }
                tvPriceNew.text = getString(R.string.currency_symbol, newprice)

                buttonAddToCart.setOnClickListener {
                    viewModel.addCart(args.product.id)
                    requireContext().showToast(getString(R.string.snackbar_message_cart))
                }


            }

        }
    }

    private fun initDetailButton() {
        when (isDetailopen) {
            false -> {

                startRotateAnimation()
                startToggleDescriptionAnim()
            }

            else -> {
                startRotateAnimation()
                startRotateAnimation()
            }
        }
    }

    private fun setFavoriteBackground(ischanged: Boolean) {
        val color = if (ischanged) {
            ContextCompat.getColor(binding.root.context, R.color.orange)
        } else {
            ContextCompat.getColor(binding.root.context, R.color.white)
        }

        binding.includedLayout.cvFavorite.setCardBackgroundColor(
            color
        )


    }


    private fun startRotateAnimation() {

        val targetRotation = if (isRotated) 0f else 180f
        val rotateAnimation = RotateAnimation(
            if (isRotated) 180f else 0f, targetRotation,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 300
        rotateAnimation.repeatCount = 0
        rotateAnimation.fillAfter = true

        rotateAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                isRotated = !isRotated
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        binding.imDetatilButton.startAnimation(rotateAnimation)
    }
    private fun startToggleDescriptionAnim() {
        val minheight = DETAIL_CARD_MIN_HEIGHT
        val maxheight = DETAIL_CARD_MAX_HEIGHT
        val startHeight = if (isExpanded) maxheight else minheight
        val endHeight = if (isExpanded) minheight else maxheight

        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.addUpdateListener { valueAnimator ->
            val layoutParams = binding.clDescriptionCard.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            binding.clDescriptionCard.layoutParams = layoutParams
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isExpanded = !isExpanded
                if (isExpanded){
                    binding.tvTitle.maxLines = Int.MAX_VALUE
                }else{
                    binding.tvTitle.maxLines = 1
                }
            }

        })

        animator.duration = 300
        animator.start()
    }
}