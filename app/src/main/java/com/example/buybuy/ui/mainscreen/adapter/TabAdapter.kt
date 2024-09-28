package com.example.buybuy.ui.mainscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buybuy.R
import com.example.buybuy.databinding.ItemTabLayouthBinding

class TabAdapter : ListAdapter<String, TabAdapter.TabViewHolder>(ProductComparatorTab()) {


    var onTabSelected: (String) -> Unit = {}


    private var selectedPosition = 0

    class TabViewHolder( binding: ItemTabLayouthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val itemTab = binding.cvTab
        val itemText = binding.tabTitle


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding =
            ItemTabLayouthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    class ProductComparatorTab : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private fun selectCardBackgroundColor(context: Context, isSelected: Boolean): Int {
        return if (isSelected) ContextCompat.getColor(context, R.color.orange)
        else ContextCompat.getColor(context, R.color.white)
    }
}