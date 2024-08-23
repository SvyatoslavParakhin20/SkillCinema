package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FilmSeasonsViewBinding
import com.skillcinema.entity.FilmSeasonsDto
import kotlin.properties.Delegates

class FilmSeasonsAdapter(
    val onItemClick: (Int,String) -> Unit
) : RecyclerView.Adapter<FilmSeasonsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmSeasonsViewBinding) :
        RecyclerView.ViewHolder(binding.root)
    private lateinit var seasonsInfo: FilmSeasonsDto
    private var filmId by Delegates.notNull<Int>()
    private var seriesName by Delegates.notNull<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            FilmSeasonsViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            this.seasonsHeader.text = if (seasonsInfo.items.isNotEmpty()) root.context.getString(R.string.seasons_header) else ""
            this.seasonsHeader.visibility = if (seasonsInfo.items.isNotEmpty()) View.VISIBLE else View.GONE
            this.seasonsAll.text = if(seasonsInfo.items.isNotEmpty()) root.context.getString(R.string.all) else ""
            this.seasonsAll.visibility = if (seasonsInfo.items.isNotEmpty())View.VISIBLE else View.GONE
            this.seasonsAll.setOnClickListener{onItemClick(filmId,seriesName)}
            val seasonsQty:String = if (seasonsInfo.total != 0) seasonsInfo.total.toString() else ""
            var seriesCounter = 0
            if(seasonsInfo.items.isNotEmpty()) for (season in seasonsInfo.items) seriesCounter+= season.episodes!!.size
            val seriesQty = if (seasonsInfo.total != 0) seriesCounter.toString() else ""
            val season = when(seasonsInfo.items.size%10){
                1 -> "сезон"
                in 2..4 -> "сезона"
                in 5..9, 0 -> "сезонов"
                else -> "сезонов"
            }
            val series = when(seriesCounter%10){
                1 -> "серия"
                in 2..4 -> "серии"
                in 5..9, 0 -> "серий"
                else -> "серий"
            }
            val description = "$seasonsQty $season, $seriesQty $series"
            this.seasonsDescription.text = if (seasonsInfo.items.isNotEmpty()) description else ""
            this.seasonsDescription.visibility= if (seasonsInfo.items.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
    override fun getItemCount(): Int {
        return 1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: FilmSeasonsDto,kinopoiskId:Int, filmName:String) {
        this.seasonsInfo = info
        this.filmId = kinopoiskId
        this.seriesName = filmName
        notifyDataSetChanged()
    }
}