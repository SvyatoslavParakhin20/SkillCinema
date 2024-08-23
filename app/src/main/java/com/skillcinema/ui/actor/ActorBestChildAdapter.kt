package com.skillcinema.ui.actor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.ActorBestFilmViewBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmInfo

class ActorBestChildAdapter (
    val onItemClick: (Int) -> Unit,
    filmData: List<FilmInfo>,
    general: ActorGeneralInfoDto
) :
    RecyclerView.Adapter<ActorBestChildAdapter.DataViewHolder>() {
    private var filmList: List<FilmInfo> = ArrayList()
    private var generalInfo: ActorGeneralInfoDto
    init {
        this.filmList = filmData
        this.generalInfo = general
    }
    inner class DataViewHolder(val binding:ActorBestFilmViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                filmList[bindingAdapterPosition].kinopoiskId?.let{ id ->
                    onItemClick(id)}
            }
        }
        fun bind(result: FilmInfo,holder:DataViewHolder) {
            holder.binding.apply {
                filmNameTextView.text = when {
                    result.nameRu != null -> result.nameRu
                    result.nameEn != null -> result.nameEn.toString()
                    result.nameOriginal != null -> result.nameOriginal.toString()
                    else -> ""
                }
                var rating = ""
                if (result.ratingKinopoisk != null) {
                    rating = result.ratingKinopoisk.toString()
                } else {
                    for (film in generalInfo.films!!) {
                        if (result.kinopoiskId == film.filmId && film.rating != null) {
                            rating = film.rating.toString()
                        }
                    }
                }
                ratingFrame.visibility = if (rating != "") View.VISIBLE else View.GONE
                ratingTextView.text = rating
                val posterUrlPreview = result.posterUrlPreview
                Glide.with(binding.root.context).load(posterUrlPreview).into(filmImageView)
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
        ActorBestFilmViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(result = filmList[position],holder = holder)
    }
    override fun getItemCount(): Int = filmList.size
}