package com.example.buybuy.domain.model.mapper

interface BaseMapper<M,T>{

    fun mapToModel(model:M):T

    fun mapToModelList(models:List<M>):List<T>{
        return models.mapTo(mutableListOf(),::mapToModel)
    }
}