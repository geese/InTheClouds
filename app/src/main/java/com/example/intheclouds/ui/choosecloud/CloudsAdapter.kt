package com.example.intheclouds.ui.choosecloud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intheclouds.R
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.util.DefaultViewHolder

class CloudsAdapter(private var cloudList: ArrayList<Cumulus.CloudImage>)
    :RecyclerView.Adapter<DefaultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        return DefaultViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cloud_row_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cloudList.size
    }

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val cloudImage: Cumulus.CloudImage = cloudList[position]
        Glide.with(holder.itemView.context)
            .load(cloudImage.url)
            .into(holder.getImage(R.id.cloud_image))
    }
}