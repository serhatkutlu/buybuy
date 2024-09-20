package com.example.buybuy.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.buybuy.data.model.data.ProfileOption
import com.example.buybuy.databinding.ItemProfileBinding
import com.example.buybuy.util.ProfileDiffUtil
import com.example.buybuy.util.setImage

class ProfileAdapter():ListAdapter<ProfileOption,ProfileAdapter.ProfileViewHolder>(ProfileDiffUtil()) {

    class ProfileViewHolder(private val binding:ItemProfileBinding):ViewHolder(binding.root){
        fun bind(item:ProfileOption){
            binding.ivIcon.setImageResource(item.iconResId)
            binding.tvTitle.text=item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding=ItemProfileBinding.inflate(LayoutInflater.from(parent.context))
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int =currentList.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}