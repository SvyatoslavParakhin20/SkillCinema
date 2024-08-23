package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FilmGalleryChildRvBinding
import com.skillcinema.entity.FilmGalleryDto

class FilmGalleryParentAdapter(
    val onItemClickGalleryParent:(Int) -> Unit,
    val onItemClickGalleryChild:(String) -> Unit
) : RecyclerView.Adapter<FilmGalleryParentAdapter.ViewHolderGallery>() {
    private lateinit var images: FilmGalleryDto
    private var kinopoiskId = 0
    inner class ViewHolderGallery(val binding:FilmGalleryChildRvBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(result: FilmGalleryDto) {
            binding.apply {
                val takenImages = result.items?.take(20)
                childRecyclerView.adapter =
                    FilmGalleryChildAdapter(
                        onItemClickChild = {url -> onItemClickGalleryChild(url)},
                        takenImages!!
                    )
                childRecyclerView.layoutManager =
                    GridLayoutManager(root.context, 1, GridLayoutManager.HORIZONTAL, false)
                galleryAll.text = if (result.total!! > 4) "${result.total} >" else ""
                galleryAll.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
                galleryAll.setOnClickListener {onItemClickGalleryParent(kinopoiskId)}
                val header = root.context.getString(R.string.gallery_header)
                galleryHeader.text = if (result.items.isNotEmpty()) header else ""
                galleryHeader.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderGallery (
        FilmGalleryChildRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: ViewHolderGallery, position: Int) {
        holder.bind(images)
    }
    override fun getItemCount(): Int = 1
    @SuppressLint("NotifyDataSetChanged")
    fun addData(gallery: FilmGalleryDto,filmId:Int) {
        images = gallery
        kinopoiskId = filmId
        notifyDataSetChanged()
    }
}