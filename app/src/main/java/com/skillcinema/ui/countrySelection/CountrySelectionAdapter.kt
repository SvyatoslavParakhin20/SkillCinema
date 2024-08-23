package com.skillcinema.ui.countrySelection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.data.FilterCountryDto
import com.skillcinema.databinding.CountrySelectionCountryViewBinding

class CountrySelectionAdapter (
val onClickCountry: (Int) -> Unit
): RecyclerView.Adapter<ViewHolder>() {
    var countries: List<FilterCountryDto> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = CountrySelectionCountryViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = countries[position]
        holder.binding.apply {
            countryName.text = result.country
                root.setOnClickListener {
                    result.id?.let { it1 -> onClickCountry(it1) }
            }
        }
    }
    override fun getItemCount(): Int = countries.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<FilterCountryDto>) {
        countries = list
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        countries = listOf()
        notifyDataSetChanged()
    }
}
class ViewHolder(val binding: CountrySelectionCountryViewBinding) :
    RecyclerView.ViewHolder(binding.root)