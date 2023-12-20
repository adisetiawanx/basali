package com.capstone.basaliproject.ui.scan.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.databinding.HistoryEachItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ItemAdapter :
    PagingDataAdapter<DataItem, ItemAdapter.ItemViewHolder>(DataItemDiffCallback) {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = HistoryEachItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = getItem(position)
        model?.let {
            holder.bind(it)
        }
    }

    inner class ItemViewHolder(private val binding: HistoryEachItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {
            val scannedAt = item?.scannedAt
            val (month, year) = extractMonthYear(scannedAt)
            binding.apply {
                textViewMonth.text = month
                textViewYear.text = year

                linearLayout.setOnClickListener{
                    if (item != null) {
                        itemClickListener?.onItemClicked(item)
                    }
                }
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    fun extractMonthYear(scannedAt: String?): Pair<String, String> {
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
        val date = dateFormat.parse(scannedAt)

        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        val month = monthFormat.format(date)
        val year = yearFormat.format(date)

        return Pair(month, year)
    }

    object DataItemDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.predictionId == newItem.predictionId
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    interface ItemClickListener {
        fun onItemClicked(item: DataItem)
    }
}
