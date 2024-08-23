package com.skillcinema.ui.filmography

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmographyFilmViewBinding
import com.skillcinema.entity.FilmInfo

class FilmographyChippedAdapter(
    val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<FilmographyChippedAdapter.DataViewHolder>() {
    private var filmList: MutableList<FilmInfo> = ArrayList()
    inner class DataViewHolder(val binding:FilmographyFilmViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
             binding.root.setOnClickListener {
                 filmList[bindingAdapterPosition].kinopoiskId?.let { id -> onItemClick(id) }
            }
        }
        fun bind(result: FilmInfo) {
            binding.apply {
                filmNameTextView.text = when {
                    result.nameRu != null -> result.nameRu
                    result.nameEn != null -> result.nameEn.toString()
                    result.nameOriginal != null -> result.nameOriginal.toString()
                    else -> ""
                }
                var rating = ""
                if (result.ratingKinopoisk != null) {
                    rating = result.ratingKinopoisk.toString()
                }
                ratingFrame.visibility = if (rating != "") View.VISIBLE else View.GONE
                ratingTextView.text = rating
                val posterUrlPreview = result.posterUrlPreview
                Glide.with(root.context).load(posterUrlPreview).into(filmImageView)
                var genres = ""
                if (result.genres.size == 1) genres += result.genres[0].genre
                else {
                    for (i in result.genres) {
                        genres += i.genre + ", "
                    }
                }
                filmGenreTextView.text =
                    if (result.genres.size != 1) genres.dropLast(2) else genres
                alreadyWatched.visibility =
                    if (result.isWatched) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        FilmographyFilmViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }
    override fun getItemCount(): Int = filmList.size
    @SuppressLint("NotifyDataSetChanged")
    fun removeData (){
        filmList.clear()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData (filmData: List<FilmInfo>) {
        this.filmList = filmData.toMutableList()
        notifyDataSetChanged()
    }
}