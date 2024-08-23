package com.skillcinema.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.FragmentProfileFilmParentAdapterBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film

class ParentFilmListAdapter (
    val onClickOpenFullCollection: (CollectionWIthFilms) -> Unit,
    val onClickCLeanCollection: (String) -> Unit,
    val onClickFilm: (Film) -> Unit

):
    RecyclerView.Adapter<ViewHolder>() {
    var collections: List<CollectionWIthFilms> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        FragmentProfileFilmParentAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = collections[0]
        val takenFilms = if (result.films.size < 20) result.films else result.films.take(20)
        val childFilmListAdapter =
            ChildFilmListAdapter(
                filmData = collections,
                onClickCleanHistory = {cleanCollection ->
                    onClickCLeanCollection(cleanCollection)
                    holder.binding.all.text = "0"
                    holder.binding.collectionRecyclerView.visibility = View.GONE
                },
                onClickFilm = { film -> onClickFilm(film)}
            )
                holder.binding.apply {
                    collectionRecyclerView.visibility = if (result.films.isNotEmpty()) View.VISIBLE else View.GONE
                    header.text = result.collection.name
                    all.text = "${takenFilms.size}  >"
                    collectionRecyclerView.adapter = childFilmListAdapter
                    all.setOnClickListener {
                        onClickOpenFullCollection(result)
                    }
                }
            }
    override fun getItemCount(): Int = collections.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<CollectionWIthFilms>) {
        collections = list
        notifyDataSetChanged()
    }
}
class ViewHolder(val binding: FragmentProfileFilmParentAdapterBinding) :
    RecyclerView.ViewHolder(binding.root)