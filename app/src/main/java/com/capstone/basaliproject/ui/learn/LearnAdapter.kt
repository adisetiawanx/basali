package com.dicoding.picodiploma.loginwithanimation.view.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.databinding.LearnItemBinding
import com.capstone.basaliproject.ui.learn.model.LearnModel

class LearnAdapter(private val clickListener: ItemClickListener) :
    ListAdapter<LearnModel, LearnAdapter.MyViewHolder>(DIFF_CALLBACK) {

    interface ItemClickListener {
        fun onItemClick(items: LearnModel, view: LearnItemBinding)
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
        val binding = LearnItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val users = getItem(position)
        holder.bind(users)
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(users, holder.binding)
        }
        holder.binding.btnView.setOnClickListener {
            clickListener.onItemClick(users, holder.binding)
        }
    }

    class MyViewHolder(val binding: LearnItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(learnItem: LearnModel) {

            binding.apply {
                imageviewNested.setImageResource(learnItem.image)
                titleNested.text = learnItem.title
                tvDesc.text = learnItem.desc
                btnView.setOnClickListener {

                }

            }
        }
    }
}