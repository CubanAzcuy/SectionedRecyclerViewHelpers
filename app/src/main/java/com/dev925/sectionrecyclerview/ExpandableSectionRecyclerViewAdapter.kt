package com.dev925.sectionrecyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dev925.sectionrecyclerview.viewholder.BodyRecyclerViewHolder
import com.dev925.sectionrecyclerviewhelper.holder.CacheableSectionHolder
import com.dev925.sectionrecyclerviewhelper.listeners.DataSetModifiedListener
import com.dev925.sectionrecyclerviewhelper.section.Section

class ExpandableSectionRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sectionHolder = CacheableSectionHolder(object : DataSetModifiedListener {
        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onDataSetChange() {
            notifyDataSetChanged()
        }

    })

    override fun getItemCount(): Int {
        return sectionHolder.size
    }

    override fun getItemViewType(position: Int): Int {
        return sectionHolder[position]?.let {
            it.type
        } ?: run {
            -1
        }
    }

    enum class ViewHolders {
        HEADER, BODY, LOADING
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BodyRecyclerViewHolder.newInstance(viewGroup)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is BodyRecyclerViewHolder ->  {
                val nnn = getDataFor(position)
                val data = nnn as String
                holder.bind(data)
                holder.itemView.setOnClickListener {
                    val section = sectionHolder.getSection(position)
                    sectionHolder.expandSection(section)
                }
            }
        }
    }

    private fun getDataFor(position: Int): Any? {
        return sectionHolder[position]?.data
    }

    fun addSection(section: Section) {
        sectionHolder.add(section)
    }

}