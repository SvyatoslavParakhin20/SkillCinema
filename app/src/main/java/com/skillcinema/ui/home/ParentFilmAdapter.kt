package com.skillcinema.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.data.FooterAdapter
import com.skillcinema.databinding.HomeFilmRecyclerviewBinding
import com.skillcinema.entity.FilmsDto
import me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK
import me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

open class ParentFilmAdapter (
    val onChildItemClick: (Int) -> Unit,
    val onParentItemClick: (Int,String) -> Unit
) :
    RecyclerView.Adapter<ParentFilmAdapter.DataViewHolder>() {
    var filmCategoriesList: List<FilmsDto> = listOf()
    val bundle = Bundle()
    inner class DataViewHolder(val binding:HomeFilmRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {parentItemClick()}
        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(result: FilmsDto,holder:DataViewHolder) {
            holder.binding.apply {
                homeTextViewPremieres.text = result.category
                val childMembersAdapter =
                    ChildFilmAdapter(result.category!!, result.items!!.take(20))
                    {onChildItem -> onChildItemClick(onChildItem) }
                val footerAdapter = FooterAdapter()
                childRecyclerView.layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                val concatAdapter = ConcatAdapter(childMembersAdapter, footerAdapter)
                childRecyclerView.adapter = concatAdapter
                if (childRecyclerView.itemDecorationCount == 0) {
                    childRecyclerView.addItemDecoration(RecyclerItemDecoration(21, 8, true))
                }
                val decorator = OverScrollDecoratorHelper.setUpOverScroll(
                    childRecyclerView,
                    OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
                )
                decorator.setOverScrollStateListener { _, oldState, newState ->
                    if (newState == STATE_BOUNCE_BACK && oldState != STATE_DRAG_START_SIDE) parentItemClick()
                }
                childRecyclerView.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager =
                            LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                        val itemCount = recyclerView.layoutManager?.itemCount
                        val lastVisible = layoutManager!!.findLastVisibleItemPosition()
                        if (itemCount == lastVisible + 1) {
                            premiereShowAllLayout.visibility =
                                View.VISIBLE
                        } else premiereShowAllLayout.visibility = View.GONE
                    }
                })
            }
        }
        private fun parentItemClick(){
            onParentItemClick(filmCategoriesList[bindingAdapterPosition].filterCategory!!,
                filmCategoriesList[bindingAdapterPosition].category!!)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
            HomeFilmRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmCategoriesList[position],holder)
    }
    override fun getItemCount(): Int = filmCategoriesList.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<FilmsDto>) {
        filmCategoriesList = list
        notifyDataSetChanged()
    }
}
