package com.skillcinema.ui.galleryFull

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.skillcinema.databinding.GalleryFullFilmViewBinding
import com.skillcinema.entity.FilmGalleryItemDto

class GalleryChippedAdapter (
    private val onClick: (FilmGalleryItemDto) -> Unit,
    ) : PagingDataAdapter<FilmGalleryItemDto, GalleryPagedViewHolder>(DiffUtilCallback()) {
    override fun getItemViewType(position: Int): Int {
        return when (position % 3) {
            in 0..1 -> TYPE_HALF
            2 -> TYPE_FULL
            else -> 1 //never
        }
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPagedViewHolder {
            val holder = GalleryPagedViewHolder(GalleryFullFilmViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        val displayMetrics = holder.binding.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        holder.itemView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val lp = holder.itemView.layoutParams
                if (lp is StaggeredGridLayoutManager.LayoutParams) {
                    val sglp: StaggeredGridLayoutManager.LayoutParams = lp
                    when (viewType) {
                        TYPE_HALF -> {
                            sglp.isFullSpan = false
                            sglp.width = screenWidth / 2 -8
                            sglp.height = screenWidth / 4
                        }
                        TYPE_FULL -> {
                            sglp.width = screenWidth
                            sglp.height = screenWidth/2
                            sglp.isFullSpan = true
                        }
                    }
                    holder.itemView.layoutParams = sglp
                    val lm = (parent as RecyclerView).layoutManager as StaggeredGridLayoutManager
                    lm.invalidateSpanAssignments()
                    holder.itemView.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
                return false
            }
        })
        return holder
        }
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: GalleryPagedViewHolder, position: Int) {
        val item = getItem(position)
            Glide.with(holder.binding.root.context)
                .load(item?.imageUrl)
                .centerCrop()
                .into(holder.binding.posterImageView)
            holder.binding.root.setOnClickListener {
                onClick(item!!)
            }
    }
    companion object {
        private const val TYPE_FULL = 0
        private const val TYPE_HALF = 1
    }
}
    class DiffUtilCallback : DiffUtil.ItemCallback<FilmGalleryItemDto>() {
        override fun areItemsTheSame(oldItem: FilmGalleryItemDto, newItem: FilmGalleryItemDto): Boolean =
            oldItem.imageUrl == newItem.imageUrl
        override fun areContentsTheSame(oldItem: FilmGalleryItemDto, newItem: FilmGalleryItemDto): Boolean =
            oldItem.imageUrl == newItem.imageUrl
    }
    class GalleryPagedViewHolder(val binding: GalleryFullFilmViewBinding) : RecyclerView.ViewHolder(binding.root)
