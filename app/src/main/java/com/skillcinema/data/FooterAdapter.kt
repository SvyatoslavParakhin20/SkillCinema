package com.skillcinema.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.FooterViewBinding

class FooterAdapter :
    RecyclerView.Adapter<FooterAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FooterViewBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FooterViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
        }
    }
    override fun getItemCount(): Int {
        return 1
    }
}