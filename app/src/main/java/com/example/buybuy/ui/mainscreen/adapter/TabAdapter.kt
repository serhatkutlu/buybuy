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

class TabAdapter() : ListAdapter<String, TabAdapter.TabViewHolder>(ProductComparatorTab()) {


    var onTabSelected: (String) -> Unit = {}


    private var selectedposition = 0

    class TabViewHolder(private val binding: ItemTabLayouthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val itemtab = binding.cvTab
        val itemtext = binding.tabTitle


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding =
            ItemTabLayouthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.itemtext.text = getItem(holder.adapterPosition)
        val context = holder.itemView.context


        val color = selectCardBackgroundColor(context, holder.adapterPosition == selectedposition)

        holder.itemtab.setCardBackgroundColor(color)

        holder.itemView.setOnClickListener {
            selectedposition = holder.adapterPosition
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

    private fun selectCardBackgroundColor(comtext: Context, isSelected: Boolean): Int {
        return if (isSelected) ContextCompat.getColor(comtext, R.color.orange)
        else ContextCompat.getColor(comtext, R.color.white)
    }
}