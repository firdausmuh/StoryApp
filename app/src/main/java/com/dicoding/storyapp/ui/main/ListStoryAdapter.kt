package com.dicoding.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ItemStoriesBinding
import com.dicoding.storyapp.service.response.ListStoryItem

class ListStoryAdapter(private val onItemClick: (ListStoryItem) -> Unit) :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_ITEM_CALLBACK) {

    inner class ListViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                tvTitle.text = story.name
                tvDescItem.text = story.description
                Glide.with(itemView.context)
                    .load(story.userProfileImage)
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.keyra)
                    ).into(imgUser)

                Glide.with(itemView.context)
                    .load(story.photo)
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(ivStory)

                itemView.setOnClickListener {
                    onItemClick(story)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldStory: ListStoryItem,
                newStory: ListStoryItem
            ): Boolean {
                return oldStory == newStory
            }

            override fun areContentsTheSame(
                oldStory: ListStoryItem,
                newStory: ListStoryItem
            ): Boolean {
                return oldStory.name == newStory.name &&
                        oldStory.description == newStory.description &&
                        oldStory.photo == newStory.photo
            }
        }
    }
}
