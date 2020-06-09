package com.example.intheclouds.ui.choosecloud

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intheclouds.R
import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.util.toByteArray
import kotlinx.android.synthetic.main.cloud_row_item.view.*

class ChooseCloudsRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Pixabay.CloudImage>() {

        override fun areItemsTheSame(
            oldItem: Pixabay.CloudImage,
            newItem: Pixabay.CloudImage
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Pixabay.CloudImage,
            newItem: Pixabay.CloudImage
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CloudImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.cloud_row_item,
                    parent,
                    false
                ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CloudImageViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Pixabay.CloudImage>) {
        differ.submitList(list)
    }

    class CloudImageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Pixabay.CloudImage) = with(itemView) {
            this.setOnClickListener {
                val bitmap = ((itemView.findViewById(R.id.choose_cloud_image_view) as ImageView)
                    .drawable as BitmapDrawable)
                    .bitmap
                interaction?.onItemSelected(
                    CaptionedCloud(
                        url = item.url ?: "",
                        byteArray = bitmap.toByteArray(),
                        caption = ""
                    )
                )
            }

            Glide.with(itemView.context)
                .load(item.url?.replace("640", "340"))
                .into(itemView.choose_cloud_image_view)
        }
    }

    // interface for detecting clicks
    interface Interaction {
        fun onItemSelected(cloud: CaptionedCloud)
    }
}