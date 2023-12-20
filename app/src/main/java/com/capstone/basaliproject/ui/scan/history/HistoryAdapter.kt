package com.capstone.basaliproject.ui.scan.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.databinding.HistoryNestedItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private val listHistory = ArrayList<DataItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback{
        fun onItemClicked(data: DataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryNestedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistory.size

    fun setList(newList: List<DataItem>){
        val diffResult = DiffUtil.calculateDiff(MainDiffCallback(listHistory, newList))
        listHistory.clear()
        listHistory.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: HistoryNestedItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: DataItem){
            Glide.with(binding.imageviewNested.context)
                .load(item.imgaeUrl)
                .into(binding.imageviewNested)
            binding.titleNested.text = item.predictionResult
            binding.dateNested.text = item.scannedAt

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = listHistory[position]
        holder.bind(user)

    }

    class MainDiffCallback(
        private val oldList: List<DataItem>,
        private val newList: List<DataItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].predictionId == newList[newItemPosition].predictionId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}