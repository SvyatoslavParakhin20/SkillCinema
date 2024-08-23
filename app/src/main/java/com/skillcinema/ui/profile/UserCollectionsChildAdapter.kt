package com.skillcinema.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentProfileCollectionViewBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Collections

class UserCollectionsChildAdapter (
    val context: Context,
    private val onClickCollection: (CollectionWIthFilms) -> Unit,
    private val onClickDeleteCollection: (String) -> Unit
):
    RecyclerView.Adapter<CollectionViewHolder>() {
    var collections: MutableList<CollectionWIthFilms> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = FragmentProfileCollectionViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.collectionFrame.layoutParams.width = screenWidth / 2-28
        view.collectionFrame.layoutParams.height = screenWidth / 2-28
        return CollectionViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val result = collections[position]
        holder.binding.apply {
            collectionIcon.setImageResource(
                when(result.collection.name){
                    Collections.LIKED.rusName -> R.drawable.liked
                    Collections.TO_WATCH.rusName -> R.drawable.in_to_watch
                    else -> R.drawable.profile
                }
            )
            closeButton.visibility = if (result.collection.name == Collections.LIKED.rusName ||
                result.collection.name == Collections.TO_WATCH.rusName) View.GONE else View.VISIBLE
            closeButton.setOnClickListener {
                onClickDeleteCollection(result.collection.name)
                collections.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,collections.size)
            }
            collectionNameTextView.text = result.collection.name
            collectionSizeTextView.text = " ${result.films.size} "
            root.setOnClickListener { onClickCollection(collections[position]) }
        }
    }
    override fun getItemCount(): Int = collections.size
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<CollectionWIthFilms>) {
        collections = list.toMutableList()
        notifyDataSetChanged()
    }
    fun addCollection(collection: CollectionWIthFilms) {
        collections.add(collection)
        notifyItemInserted(collections.size-1)
    }
}
class CollectionViewHolder(val binding: FragmentProfileCollectionViewBinding) : RecyclerView.ViewHolder(binding.root)