package com.skillcinema.ui.actorsFull

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.ActorsFullActorViewBinding
import com.skillcinema.entity.ActorDto

class ActorsAdapter(
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {
    private var actorsList: List<ActorDto> = listOf()
    inner class ActorViewHolder( val binding: ActorsFullActorViewBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
             binding.root.setOnClickListener{
                 actorsList[bindingAdapterPosition].staffId?.let { id -> onItemClick(id)
                 }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ActorViewHolder {
        val view = ActorsFullActorViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val displayMetrics = parent.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2-20
        return ActorViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActorsAdapter.ActorViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(root.context)
                .load(actorsList[position].posterUrl)
                .centerCrop()
                .into(actorImageView)
            actorNameTextView.text = if (actorsList[position].nameRu != null)
                actorsList[position].nameRu else actorsList[position].nameEn ?: ""
            actorNickTextView.text = if (actorsList[position].description!=null) actorsList[position].description else actorsList[position].professionText ?: ""
        }
    }
    override fun getItemCount(): Int {
        return actorsList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData (actors:List<ActorDto>) {
        this.actorsList = actors
        notifyDataSetChanged()
    }
}
