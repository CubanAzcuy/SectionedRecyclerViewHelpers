package com.dev925.sectionrecyclerviewhelper.section

import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.utils.Cache

abstract class CacheableExpandableSection: ExpandableSection() {

    protected lateinit var sectionLoadingRow: Row

    var cacheState = Cache.NEW
        protected set

    override val size: Int
        get() = if (cacheState == Cache.LOADING_FOREGROUND) {
            2
        } else {
            super.size
        }

    override fun get(i: Int): Row? {
        return if (cacheState == Cache.LOADING_FOREGROUND && i != 0) {
            sectionLoadingRow
        } else {
            super.get(i)
        }
    }

    fun setForeground() {
        when (cacheState) {
            Cache.LOADING_BACKGROUND -> cacheState = Cache.LOADING_FOREGROUND
            Cache.CACHED_BACKGROUND -> {
                cacheState = Cache.CACHED_FOREGROUND
                expanded = true
            }
        }
    }

    fun setBackground() {
        when (cacheState) {
            Cache.LOADING_FOREGROUND -> cacheState = Cache.LOADING_BACKGROUND
            Cache.CACHED_FOREGROUND -> cacheState = Cache.CACHED_BACKGROUND
        }

        expanded = false
    }

    fun hideLoading() {
        cacheState = Cache.CACHED_FOREGROUND
        expanded = true
    }

    fun pause() {
        cacheState = Cache.PAUSE
        expanded = false
    }

    fun unpause() {
        cacheState = Cache.LOADING_FOREGROUND
    }


    fun preloadData(updatable: UpdatableListener<Any?>) {
        cacheState = Cache.LOADING_FOREGROUND
        loadData(updatable)
    }

    abstract fun loadData(updatable: UpdatableListener<Any?>)

    protected fun loadingComplete(updatable: UpdatableListener<Any?>) {

        if (cacheState != Cache.PAUSE) {
            try {
                cacheState = Cache.LOADING_BACKGROUND
                updatable.update(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            cacheState = Cache.CACHED_BACKGROUND
        }

    }

    fun expand(updatable: UpdatableListener<Any?>) {

        if (sectionRows.size > 0) {
            cacheState = Cache.CACHED_FOREGROUND
            expanded = true
        }

        when (cacheState) {
            Cache.NEW -> {
                cacheState = Cache.LOADING_FOREGROUND
                preloadData(updatable)
            }
            Cache.LOADING_BACKGROUND -> cacheState = Cache.LOADING_FOREGROUND
        }
    }

    fun clearCache() {
        sectionRows.clear()
        expanded = false
        cacheState = Cache.NEW
    }
}
