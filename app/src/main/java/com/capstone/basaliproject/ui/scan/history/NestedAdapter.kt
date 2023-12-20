package com.capstone.basaliproject.ui.scan.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.databinding.HistoryNestedItemBinding

class NestedAdapter :
    PagingDataAdapter<DataItem, NestedAdapter.NestedViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        val binding = HistoryNestedItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NestedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
        val model = getItem(position)
        model?.let {
            holder.bind(it)
        }
    }

    inner class NestedViewHolder(private val binding: HistoryNestedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem?) {
            binding.apply {
                Glide.with(imageviewNested.context)
                    .load(item?.imgaeUrl)
                    .fitCenter()
                    .into(imageviewNested)
                titleNested.text = item?.predictionResult
                dateNested.text = item?.scannedAt
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem.predictionId == newItem.predictionId
            }
        }
    }
}
