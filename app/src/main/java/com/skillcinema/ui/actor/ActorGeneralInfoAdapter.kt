package com.skillcinema.ui.actor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.ActorGeneralInfoBinding
import com.skillcinema.entity.ActorGeneralInfoDto

class ActorGeneralInfoAdapter : RecyclerView.Adapter<ActorGeneralInfoAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ActorGeneralInfoBinding) :
        RecyclerView.ViewHolder(binding.root)
    private lateinit var actorInfo: ActorGeneralInfoDto
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(
            ActorGeneralInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
                Glide.with(root.context)
                    .load(actorInfo.posterUrl)
                    .centerCrop()
                    .into(actorImageView)
                 val staffName = actorInfo.nameRu ?: (actorInfo.nameEn ?: "")
            actorNameTextView.text = staffName
            val staffProfession = actorInfo.profession ?:""
            actorProfessionTextView.text = staffProfession
        }
    }
    override fun getItemCount(): Int {
        return 1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: ActorGeneralInfoDto) {
        this.actorInfo = info
        notifyDataSetChanged()
    }
}