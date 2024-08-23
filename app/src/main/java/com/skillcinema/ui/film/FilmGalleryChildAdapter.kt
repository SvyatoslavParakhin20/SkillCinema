package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmGalleryImageViewBinding
import com.skillcinema.entity.FilmGalleryItemDto

class FilmGalleryChildAdapter (
    val onItemClickChild:(String) -> Unit,
    images: List<FilmGalleryItemDto>
) : RecyclerView.Adapter<FilmGalleryChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmGalleryImageViewBinding) :
        RecyclerView.ViewHolder(binding.root)
    private var imagesList: List<FilmGalleryItemDto> = images
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmGalleryChildAdapter.ViewHolder {
        return ViewHolder(
            FilmGalleryImageViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmGalleryChildAdapter.ViewHolder, position: Int) {
        val url = if (imagesList[position].previewUrl != null) imagesList[position].previewUrl else imagesList[position].imageUrl
        holder.binding.apply {
            Glide.with(root.context)
                .load(url)
                .centerCrop()
                .into(galleryImageView)
        }
        holder.binding.root.setOnClickListener{
            imagesList[position].imageUrl?.let { url -> onItemClickChild(url) }
        }
    }
    override fun getItemCount(): Int {
        return imagesList.size
    }
}