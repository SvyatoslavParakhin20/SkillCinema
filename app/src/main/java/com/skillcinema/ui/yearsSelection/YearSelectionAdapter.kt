package com.skillcinema.ui.yearsSelection

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.FragmentYearSelectionYearViewBinding

class YearSelectionAdapter(
    val onClickYear: (Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    private var years: List<Int> = listOf()
    private lateinit var selectedYear: ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(FragmentYearSelectionYearViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = years[position]
        holder.binding.yearTextView.text = result.toString()
        holder.binding.yearTextView.setOnClickListener {
            selectedYear.binding.yearTextView.setBackgroundColor(Color.WHITE)
            selectedYear = holder
            holder.binding.yearTextView.setBackgroundColor(Color.GRAY)
            onClickYear(result)
        }
    }
    override fun getItemCount(): Int = years.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<Int>) {
        years = list
        notifyDataSetChanged()
    }
}
class ViewHolder(val binding: FragmentYearSelectionYearViewBinding) : RecyclerView.ViewHolder(binding.root)
