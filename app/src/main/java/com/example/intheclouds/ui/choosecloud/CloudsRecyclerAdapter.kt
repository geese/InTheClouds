package com.example.intheclouds.ui.choosecloud

import android.graphics.Bitmap
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
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.util.getBase64String
import kotlinx.android.synthetic.main.cloud_row_item.view.*

class CloudsRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cumulus.CloudImage>() {

        override fun areItemsTheSame(oldItem: Cumulus.CloudImage, newItem: Cumulus.CloudImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cumulus.CloudImage, newItem: Cumulus.CloudImage): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

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
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Cumulus.CloudImage>) {
        differ.submitList(list)
    }

    class CloudImageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Cumulus.CloudImage) = with(itemView) {
            this.setOnClickListener{
                var bitmap = ((itemView.findViewById(R.id.image_view) as ImageView)
                    .drawable as BitmapDrawable)
                    .bitmap
                interaction?.onItemSelected(adapterPosition, item, getBase64String(bitmap))
            }

            Glide.with(itemView.context)
                .load(item.url)
                .into(itemView.image_view)
        }
    }

    // interface for detecting clicks
    interface Interaction {
        fun onItemSelected(position: Int, item: Cumulus.CloudImage, encodedBitmap: String)
    }
}