package com.example.statussaver.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.statussaver.R
import com.example.statussaver.databinding.ItemMediaBinding
import com.example.statussaver.models.MEDIA_TYPE_IMAGE
import com.example.statussaver.models.MediaModel

class MediaAdapter (val list: ArrayList<MediaModel>, val context: Context) :
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (mediaModel: MediaModel) {
            binding.apply {
                Glide.with(context)
                    .load(mediaModel.pathUri)
                    .into(statusImage)

                val downloadImage = if (mediaModel.isDownloaded) {
                    R.drawable.ic_downloaded
                }
                else {
                    R.drawable.ic_download
                }

                statusDownload.setImageResource(downloadImage)

                cardStatus.setOnClickListener {
                    if (mediaModel.type == MEDIA_TYPE_IMAGE) {
                        // goto image preview activity

                    }
                    else {
                        // goto video preview
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = list.size
}