package com.dev925.sectionrecyclerviewhelper.listeners

interface DataSetModifiedListener {
    fun onChanged(position: Int, count: Int)
    fun onInserted(position: Int, count: Int)
    fun onRemoved(position: Int, count: Int)
    fun onMoved(fromPosition: Int, toPosition: Int)
    fun onDataSetChange()
}