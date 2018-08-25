package com.dev925.sectionrecyclerview.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev925.sectionrecyclerview.R
import kotlinx.android.synthetic.main.viewholder_row.view.*

class BodyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(text: String) {
        itemView.text.text = text
    }

    companion object {
        fun newInstance(parent: ViewGroup): BodyRecyclerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_row, parent, false)
            return BodyRecyclerViewHolder(view)
        }
    }

}
