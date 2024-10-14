package com.example.buybuy.domain.model.mapper

import com.example.buybuy.data.model.entity.ProductDetailEntity
import com.example.buybuy.domain.model.data.ProductDetailUI
import javax.inject.Inject

class ProductEntityToUiMapper @Inject constructor() :BaseMapper<ProductDetailEntity, ProductDetailUI> {
    override fun mapToModel(model: ProductDetailEntity): ProductDetailUI {
        return ProductDetailUI(
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
            popular = model.popular,
            onSale = model.onSale,
            color = model.color,
            model = model.model
        )
    }


}