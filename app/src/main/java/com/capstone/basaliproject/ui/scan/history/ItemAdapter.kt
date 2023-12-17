package com.capstone.basaliproject.ui.scan.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.basaliproject.R

class ItemAdapter(private val mList: List<DataModel>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_each_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = mList[position]
        holder.mTextView.text = model.itemText

        val isExpandable = model.isExpandable
        holder.expandableLayout.visibility =
            if (isExpandable) View.VISIBLE else View.GONE

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.baseline_keyboard_arrow_up)
        } else {
            holder.mArrowImage.setImageResource(R.drawable.baseline_keyboard_arrow_down)
        }

        val adapter = NestedAdapter(list)
        holder.nestedRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.setHasFixedSize(true)
        holder.nestedRecyclerView.adapter = adapter

        holder.linearLayout.setOnClickListener {
            model.customSetExpandable(!model.isExpandable)
            list.clear()
            list.addAll(model.nestedList)
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
        val expandableLayout: RelativeLayout = itemView.findViewById(R.id.expandable_layout)
        val mTextView: TextView = itemView.findViewById(R.id.textViewMonth)
        val mArrowImage: ImageView = itemView.findViewById(R.id.arro_imageview)
        val nestedRecyclerView: RecyclerView = itemView.findViewById(R.id.child_rv)
    }
}
