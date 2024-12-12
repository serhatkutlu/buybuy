package com.example.buybuy.ui.mainscreen.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemTabLayoutBinding
import com.google.android.material.color.MaterialColors

class TabAdapter : ListAdapter<String, TabAdapter.TabViewHolder>(ProductComparatorTab()) {


    var onTabSelected: (String) -> Unit = {}


    private var selectedPosition = 0

    class TabViewHolder( binding: ItemTabLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val itemTab = binding.cvTab
        val itemText = binding.tabTitle


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding =
            ItemTabLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.itemText.text = getItem(holder.adapterPosition)
        val context = holder.itemView.context


        val color = selectCardBackgroundColor(context, holder.adapterPosition == selectedPosition)

        holder.itemTab.setCardBackgroundColor(color)

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            onTabSelected(getItem(holder.adapterPosition))
            notifyDataSetChanged()
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    class ProductComparatorTab : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private fun selectCardBackgroundColor(context: Context, isSelected: Boolean): Int {
        val colorPrimary = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorPrimary,
            Color.WHITE
        )
        return if (isSelected) ContextCompat.getColor(context, R.color.orange)

        else colorPrimary
    }
}