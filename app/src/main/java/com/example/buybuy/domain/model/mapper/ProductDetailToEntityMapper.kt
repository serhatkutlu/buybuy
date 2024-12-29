package com.example.buybuy.domain.model.mapper

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.network.dto.product.ProductDetail
import javax.inject.Inject
import kotlin.random.Random

class ProductDetailToEntityMapper @Inject constructor() : BaseMapper<ProductDetail, ProductDetailEntity> {
    override fun mapToModel(model: ProductDetail): ProductDetailEntity {
        return ProductDetailEntity(
            category = model.category ?: "",
            description = model.description ?: "",
            id = model.id ?: 0,
            image = model.image ?: "",
            price = model.price?: 0,
            brand = model.brand ?: "",
            title = model.title?: "",
            discount = model.discount?: 0,
            star = generateStarRating(),
            onSale = model.onSale?: false,
            popular = model.popular?: false,
            color = model.color?: "",
            model = model.model?: "",
            isFavorite = false,
            pieceCount = 0
        )
    }

    private fun generateStarRating(): Float {
        val min = 0.0f
        val max = 5.0f

        val randomFloat = Random.nextFloat() * (max - min) + min
        val roundedFloat = (Math.round(randomFloat * 2) / 2.0).toFloat()

        return roundedFloat
    }

}

