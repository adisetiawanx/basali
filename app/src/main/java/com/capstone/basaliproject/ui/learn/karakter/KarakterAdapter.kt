package com.capstone.basaliproject.ui.learn.karakter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.databinding.KarakterEachItemBinding
import com.capstone.basaliproject.ui.learn.model.KarakterModel


class KarakterAdapter :
    ListAdapter<KarakterModel, KarakterAdapter.MyViewHolder>(DIFF_CALLBACK) {

    interface ItemClickListener {
        fun onItemClick(items: KarakterModel, view: KarakterEachItemBinding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KarakterModel>() {
            override fun areItemsTheSame(oldItem: KarakterModel, newItem: KarakterModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: KarakterModel,
                newItem: KarakterModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            KarakterEachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }

    class MyViewHolder(val binding: KarakterEachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(karakterItem: KarakterModel) {

            binding.apply {
                ivKarakter.setImageResource(karakterItem.image)
                tvTitle.text = karakterItem.title

            }
        }
    }
}