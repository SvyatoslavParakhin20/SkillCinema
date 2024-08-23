package com.skillcinema.ui.actor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.ActorBestChildRvBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmInfo

class ActorBestParentAdapter(
    val onItemParentClick: (Int) -> Unit
) : RecyclerView.Adapter<ActorBestParentAdapter.ViewHolderGallery>() {
    private lateinit var best: List<FilmInfo>
    private lateinit var generalInfo: ActorGeneralInfoDto
    inner class ViewHolderGallery(val binding: ActorBestChildRvBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(result:List< FilmInfo>,holder:ViewHolderGallery) {
            holder.binding.apply {
                childRecyclerViewBest.adapter =
                    ActorBestChildAdapter(
                        onItemClick = {filmId -> onItemParentClick(filmId)},
                        filmData = best,
                        general = generalInfo)
                childRecyclerViewBest.layoutManager =
                    GridLayoutManager(binding.root.context, 1, GridLayoutManager.HORIZONTAL, false)
                actorBestAll.text =
                    if (result.size > 10) "${root.context.getString(R.string.all)} >" else ""
                actorBestAll.visibility =
                    if (result.isNotEmpty()) View.VISIBLE else View.GONE
                val header = root.context.getString(R.string.best)
                actorBestHeader.text = if (result.isNotEmpty()) header else ""
                actorBestHeader.visibility =
                    if (result.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderGallery(
        ActorBestChildRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: ViewHolderGallery, position: Int) {
        holder.bind(best,holder)
    }
    override fun getItemCount(): Int = 1
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: List<FilmInfo>,general:ActorGeneralInfoDto) {
        best = info
        generalInfo = general
        notifyDataSetChanged()
    }
}