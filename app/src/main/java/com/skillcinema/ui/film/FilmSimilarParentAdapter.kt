package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FilmSimilarChildRvBinding
import com.skillcinema.entity.FilmSimilarsDto

class FilmSimilarParentAdapter(
    val onItemClickSimilarParent:(Int) -> Unit,
    val onItemClickSimilarChild: (Int) -> Unit
) : RecyclerView.Adapter<FilmSimilarParentAdapter.ViewHolderSimilar>() {
    private lateinit var similarFilms: FilmSimilarsDto
    private var kinopoiskId = 0
    inner class ViewHolderSimilar(val binding:FilmSimilarChildRvBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(result: FilmSimilarsDto) {
            binding.apply {
                val takenImages = result.items?.shuffled()?.take(20)
                similarChildRecyclerView.adapter =
                    FilmSimilarChildAdapter(
                        onItemClickSimilarChild = {id -> onItemClickSimilarChild(id)},
                        filmData = takenImages!!)
                similarChildRecyclerView.layoutManager =
                    GridLayoutManager(root.context, 1, GridLayoutManager.HORIZONTAL, false)
                similarAll.text = if (result.items.size > 10) "${result.total} >" else ""
                similarAll.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
                similarAll.setOnClickListener {onItemClickSimilarParent(kinopoiskId)}
                val header = root.context.getString(R.string.similar_header)
                similarHeader.text = if (result.items.isNotEmpty()) header else ""
                similarHeader.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimilar = ViewHolderSimilar (
        FilmSimilarChildRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: ViewHolderSimilar, position: Int) {
        holder.bind(similarFilms)
    }
    override fun getItemCount(): Int = 1
    @SuppressLint("NotifyDataSetChanged")
    fun addData(similar: FilmSimilarsDto,filmId:Int) {
        similarFilms = similar
        kinopoiskId = filmId
        notifyDataSetChanged()
    }
}