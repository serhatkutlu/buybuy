package com.example.buybuy.domain.model.mapper

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.model.data.ProductDetailUI
import javax.inject.Inject
import kotlin.random.Random

class ProductUiToEntityMapper @Inject constructor() :BaseMapper<ProductDetailUI,ProductDetailEntity> {
    override fun mapToModel(model: ProductDetailUI): ProductDetailEntity {
        return ProductDetailEntity(
            category = model.category,
            description = model.description,
            id = model.id,
            image = model.image,
            price = model.price,
            brand = model.brand,
            title = model.title,
            discount = model.discount,
            star = model.star,
            isFavorite = model.isFavorite,
            pieceCount = model.pieceCount,
            color = model.color,
            model = model.model,
            onSale = model.onSale,
            popular = model.popular
        )

    }

}