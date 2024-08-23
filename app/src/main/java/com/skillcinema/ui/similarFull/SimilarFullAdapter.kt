package com.skillcinema.ui.similarFull

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.SimilarFullFilmViewBinding
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmSimilarsItemDto

class SimilarFullAdapter(
    val onClickYear: (Int) -> Unit
) : RecyclerView.Adapter<SimilarFullAdapter.SimilarViewHolder>() {
    private var images: List<FilmSimilarsItemDto> = listOf()
    inner class SimilarViewHolder(val binding: SimilarFullFilmViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: FilmSimilarsItemDto) {
            binding.root.setOnClickListener { result.filmId?.let { it1 -> onClickYear(it1) } }
            binding.filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
            val url = result.posterUrlPreview ?: result.posterUrl
            Glide.with(binding.root.context)
                .load(url)
                .into(binding.filmImageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val view = SimilarFullFilmViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val displayMetrics = view.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2 - 20

        return SimilarViewHolder(view)
    }
    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind(images[position])
    }
    override fun getItemCount(): Int {
        return images.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: FilmSimilarsDto) {
        info.items?.let { this.images = it }
        notifyDataSetChanged()
    }
}