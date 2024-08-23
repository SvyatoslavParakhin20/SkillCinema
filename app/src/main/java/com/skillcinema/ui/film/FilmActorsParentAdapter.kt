package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FilmActorsChildRvBinding
import com.skillcinema.entity.ActorDto

class FilmActorsParentAdapter(
    val onItemClickChildActor: (Int) -> Unit,
    val onItemClickActorParent: (Int,Boolean,Boolean) -> Unit
) : RecyclerView.Adapter<FilmActorsParentAdapter.ViewHolderActors>() {
    private var actorsList: List<ActorDto> = listOf()
    private var otherStaff: List<ActorDto> = listOf()
    private var isSeries = false
    private var kinopoiskFilmId = 0
    inner class ViewHolderActors(val binding:FilmActorsChildRvBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(result: List<ActorDto>, position: Int,holder:ViewHolderActors) {
            holder.binding.apply {
                val takeSize = if (position == 0) 12 else 6
                val takenStaff = result.take(takeSize)
                childRecyclerView.adapter =
                    FilmActorsChildAdapter(
                        onItemClick = {staffId:Int -> onItemClickChildActor(staffId)},
                        info =  takenStaff
                    )
                val spanCount = if (position == 0) 4 else 2
                val spanCountActual = when {
                    result.isEmpty() -> 1
                    result.size > spanCount -> spanCount
                    else -> result.size
                }
                childRecyclerView.layoutManager =
                    GridLayoutManager(root.context, spanCountActual, GridLayoutManager.HORIZONTAL, false)
                actorsAll.text =
                    if (takenStaff.size < result.size) "${result.size} >" else ""
                actorsAll.visibility = if (result.isNotEmpty()) View.VISIBLE else View.GONE
                actorsAll.setOnClickListener {
                    onItemClickActorParent(kinopoiskFilmId,position==0,isSeries)
                }
                val header = when {
                    position == 0 && isSeries -> root.context.getString(R.string.series_actors_header)
                    position == 0 && !isSeries -> root.context.getString(R.string.actors_header)
                    position == 1 && isSeries -> root.context.getString(R.string.series_other_staff_header)
                    position == 1 && !isSeries -> root.context.getString(R.string.other_staff_header)
                    else -> ""
                }
                actorsHeader.text = if (result.isNotEmpty()) header else ""
                actorsHeader.visibility =
                    if (result.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderActors (
        FilmActorsChildRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: ViewHolderActors, position: Int) {
        if (position == 0) holder.bind(actorsList,position,holder) else holder.bind(otherStaff,position,holder)
    }
    override fun getItemCount(): Int = 2
    @SuppressLint("NotifyDataSetChanged")
    fun addData(actors: List<ActorDto>,others: List<ActorDto>,series: Boolean,filmId:Int) {
        actorsList = actors
        otherStaff = others
        isSeries = series
        kinopoiskFilmId = filmId
        notifyDataSetChanged()
    }
}