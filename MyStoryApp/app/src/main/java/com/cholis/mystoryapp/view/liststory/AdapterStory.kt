package com.cholis.mystoryapp.view.liststory

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cholis.mystoryapp.FormatDate
import com.cholis.mystoryapp.databinding.StoryLayoutBinding
import com.cholis.mystoryapp.response.Story
import java.util.TimeZone

class AdapterStory(
    private val listener: (
        listStoryItem: Story,
        imageView: View,
        nameView: View,
        dateView: View,
        descView: View
    ) -> Unit
) : PagingDataAdapter<Story, StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.view.root.setOnClickListener {
            if (item != null) {
                listener.invoke(
                    item,
                    holder.view.ivStoryImage,
                    holder.view.tvStoryName,
                    holder.view.tvStoryDate,
                    holder.view.tvStoryDesc
                )
            }
        }
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setOnItemClickListener(listener: (Story) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}

private var onItemClickListener: ((Story) -> Unit)? = null

class StoryViewHolder(val view: StoryLayoutBinding) : RecyclerView.ViewHolder(view.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(item: Story) {
        view.story = item

        // Menggunakan Glide untuk memuat gambar
        Glide.with(view.ivStoryImage.context)
            .load(item.photoUrl) // URL gambar dari objek Story
            .into(view.ivStoryImage)

        view.tvStoryDate.text = FormatDate.formatDate(item.createdAt, TimeZone.getDefault().id)
        view.tvStoryName.text = item.name
        view.tvStoryDesc.text = item.description

        view.root.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        view.executePendingBindings()
    }
}






