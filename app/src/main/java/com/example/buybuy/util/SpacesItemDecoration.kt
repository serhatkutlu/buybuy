package com.example.buybuy.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val spacetop: Int=0, private val spacebottom: Int=0, private val spaceleft: Int=0, private val spaceright: Int=0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spaceleft
        outRect.right = spaceright
        outRect.bottom = spacebottom
        outRect.top = spacetop


    }
}