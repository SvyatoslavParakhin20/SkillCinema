package com.skillcinema.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.databinding.FragmentProfileCreateCollectionBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Collections

class UserCollectionsAdapter (
    val context: Context,
    private val onItemClick: (CollectionWIthFilms) -> Unit,
    private val onClickCreateCollection: () -> Unit,
    private val onClickDeleteCollection: (String) -> Unit
):
    RecyclerView.Adapter<UserCollectionsAdapter.ViewHolderUCA>() {
    var collections: MutableList<CollectionWIthFilms> = mutableListOf()
    private lateinit var childAdapter: UserCollectionsChildAdapter
    inner class ViewHolderUCA(val binding: FragmentProfileCreateCollectionBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderUCA(
            FragmentProfileCreateCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    override fun onBindViewHolder(holder: ViewHolderUCA, position: Int) {
        childAdapter = UserCollectionsChildAdapter(
            context = context,
            onClickCollection = {collection -> onItemClick(collection)},
            onClickDeleteCollection = {deleteCollection -> onClickDeleteCollection(deleteCollection)}
        )
        val filteredCollections = collections.filter {
            it.collection.name!= Collections.WATCHED_LIST.rusName &&
            it.collection.name!= Collections.HISTORY.rusName
        }
        childAdapter.addData(filteredCollections)
        holder.binding.apply {
            collectionsRV.adapter = childAdapter
            createCollectionButton.setOnClickListener {
                onClickCreateCollection()
            }
        }
    }
    override fun getItemCount(): Int = 1
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<CollectionWIthFilms>) {
        collections = list.toMutableList()
        notifyDataSetChanged()
    }
    fun addCollection(collection:CollectionWIthFilms){
        childAdapter.addCollection(collection = collection)
        collections.add(collection)
    }
}
