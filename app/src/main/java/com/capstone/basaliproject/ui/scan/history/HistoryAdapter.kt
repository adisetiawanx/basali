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
            var month = item.scannedAt?.month.toString()
            val day = item.scannedAt?.day.toString()
            val year = item.scannedAt?.year.toString()
            val hour = item.scannedAt?.hour.toString()
            when (month){
                "0" -> month = "January"
                "1" -> month = "February"
                "2" -> month = "March"
                "3" -> month = "April"
                "4" -> month = "May"
                "5" -> month = "June"
                "6" -> month = "July"
                "7" -> month = "August"
                "8" -> month = "September"
                "9" -> month = "October"
                "10" -> month = "November"
                "11" -> month = "December"
            }

            val fullDate = "$day $month $year ($hour:00)"
            binding.titleNested.text = item.predictionResult
            binding.dateNested.text = fullDate

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