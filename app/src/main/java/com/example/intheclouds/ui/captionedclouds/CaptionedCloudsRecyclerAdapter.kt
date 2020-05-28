package com.example.intheclouds.ui.captionedclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import kotlinx.android.synthetic.main.captioned_cloud_row_item.view.*

class CaptionedCloudsRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CaptionedCloud>() {

        override fun areItemsTheSame(oldItem: CaptionedCloud, newItem: CaptionedCloud): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CaptionedCloud, newItem: CaptionedCloud): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CloudImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.captioned_cloud_row_item,
                    parent,
                    false
                ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CloudImageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CaptionedCloud>) {
        differ.submitList(list)
    }

    class CloudImageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CaptionedCloud) = with(itemView) {
            this.setOnClickListener{
                interaction?.onItemSelected(item)
            }

            Glide.with(itemView.context)  // use url as backup
                .load(item.byteArray)
                .into(itemView.captioned_cloud_image_view)

            itemView.caption_text_view.text = item.caption
        }
    }

    // interface for detecting clicks
    interface Interaction {
        fun onItemSelected(cloud: CaptionedCloud)
    }
}