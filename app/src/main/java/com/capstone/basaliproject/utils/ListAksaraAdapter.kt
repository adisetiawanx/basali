package com.capstone.basaliproject.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.databinding.ItemAksaraHomeBinding
import com.capstone.basaliproject.ui.home.HomeFragment
import com.capstone.basaliproject.ui.learn.model.LearnModel

class ListAksaraAdapter(private val clickListener: HomeFragment) :
    ListAdapter<LearnModel, ListAksaraAdapter.MyViewHolder>(ListAksaraAdapter.DIFF_CALLBACK) {

        interface ItemClickListener {
            fun onItemClick(items: LearnModel, view: ItemAksaraHomeBinding)
        }

        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LearnModel>() {
                override fun areItemsTheSame(oldItem: LearnModel, newItem: LearnModel): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LearnModel,
                    newItem: LearnModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = ItemAksaraHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyViewHolder(binding)
        }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val aksara = getItem(position)
            holder.bind(aksara)
            holder.itemView.setOnClickListener {
                clickListener.onItemClick(aksara, holder.binding)
            }
            holder.binding.btnView.setOnClickListener {
                clickListener.onItemClick(aksara, holder.binding)
            }
        }

        class MyViewHolder(val binding: ItemAksaraHomeBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(learnItem: LearnModel) {

                binding.apply {
                    ivItemPhoto.setImageResource(learnItem.image)
                    tvItemName.text = learnItem.title
                    tvItemDescription.text = learnItem.desc
                    btnView.setOnClickListener {

                    }

                }
            }
        }
}