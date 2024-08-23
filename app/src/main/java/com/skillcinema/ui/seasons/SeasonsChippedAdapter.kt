package com.skillcinema.ui.seasons

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.SeasonsSeasonViewBinding
import com.skillcinema.entity.Episode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeasonsChippedAdapter :
    RecyclerView.Adapter<SeasonsChippedAdapter.DataViewHolder>() {
    private var episodeList: MutableList<Episode> = ArrayList()
    inner class DataViewHolder(val binding:SeasonsSeasonViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(result: Episode) {
            val episodeNumber = result.episodeNumber
            val releaseDateInitial = result.releaseDate
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val newFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val releaseDate = LocalDate.parse(releaseDateInitial,dateFormat).format(newFormat)
            val episodeName = result.nameRu?: result.nameEn ?: ""
            binding.apply {
                episodeNameTextView.text = "$episodeNumber серия. $episodeName"
                val episodeDescription = result.synopsis?: ""
                episodeDescriptionTextView.text = episodeDescription
                episodeDescriptionTextView.visibility = if (episodeDescription == "") View.GONE else View.VISIBLE
                episodeReleaseDate.text = "Дата выхода: $releaseDate"
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        SeasonsSeasonViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(episodeList[position])
    }
    override fun getItemCount(): Int = episodeList.size
    @SuppressLint("NotifyDataSetChanged")
    fun removeData (){
        episodeList.clear()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData (episodes: List<Episode>) {
        this.episodeList = episodes.toMutableList()
        notifyDataSetChanged()
    }
}