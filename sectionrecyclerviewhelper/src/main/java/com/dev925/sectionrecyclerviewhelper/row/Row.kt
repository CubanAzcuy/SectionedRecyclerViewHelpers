package com.dev925.sectionrecyclerviewhelper.row

import android.support.annotation.NonNull

data class Row(var type: Int, @NonNull var data: Any?) {

    //This my cause collisions if you are using the same type without any data
    override fun hashCode(): Int {
        return data?.let {
            type + it.hashCode()
        } ?: run {
            type
        }
    }

    override fun equals(other: Any?): Boolean {
        return other?.hashCode() == this.hashCode()
    }

}