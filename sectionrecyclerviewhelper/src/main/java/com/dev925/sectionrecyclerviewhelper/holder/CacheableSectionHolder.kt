package com.dev925.sectionrecyclerviewhelper.holder

import com.dev925.sectionrecyclerviewhelper.listeners.DataSetModifiedListener
import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import com.dev925.sectionrecyclerviewhelper.section.CacheableExpandableSection
import com.dev925.sectionrecyclerviewhelper.utils.Cache

class CacheableSectionHolder(dataSetModifiedListener: DataSetModifiedListener) : ExpandableSectionHolder(dataSetModifiedListener) {

    override fun expandSection(position: Int) {
        if(sections[position] is CacheableExpandableSection) {
            (sections[position] as CacheableExpandableSection).let { section ->
                val relativePosition = getRelativePosition(position) + 1

                when (section.cacheState) {
                    Cache.NEW -> attemptToGetCache(relativePosition, section, position)
                    Cache.LOADING_BACKGROUND -> expandFromLoading(relativePosition, section)
                    Cache.CACHED_BACKGROUND -> expandFromCache(relativePosition, section)
                    Cache.PAUSE -> expandFromPause(relativePosition, section)
                    else -> collapseSection(position)
                }
            }
        } else {
            super.expandSection(position)
        }
    }

    override fun collapseSection(position: Int) {
        if(sections[position] is CacheableExpandableSection) {
            (sections[position] as CacheableExpandableSection).let { section ->
                val relativePosition = getRelativePosition(position) + 1
                when (section.cacheState) {
                    Cache.LOADING_FOREGROUND -> collapseFromLoading(relativePosition, section)
                    Cache.CACHED_FOREGROUND -> collapseFromDefault(relativePosition, section)
                    else -> expandSection(position)
                }
            }
        } else {
            super.collapseSection(position)
        }
    }

    private fun expandFromLoading(relativePosition: Int, section: CacheableExpandableSection) {

        section.hideLoading()
        val rowsToAdd = section.size - 1
        modifiedDataSetListener?.onChanged(relativePosition, 1)
        if (rowsToAdd > 1) {
            modifiedDataSetListener?.onInserted(relativePosition + 1, rowsToAdd - 1)
        }

    }

    private fun attemptToGetCache(relativePosition: Int, section: CacheableExpandableSection, orginalPostition: Int) {

        section.expand(object : UpdatableListener<Any?> {
            @Throws(Exception::class)
            override fun update(value: Any?) {
                expandSection(orginalPostition)
            }
        })

        val rowsToAdd = section.size - 1
        modifiedDataSetListener?.onInserted(relativePosition, rowsToAdd)
    }

    private fun expandFromPause(relativePosition: Int, section: CacheableExpandableSection) {
        section.unpause()
        val rowsToAdd = section.size - 1
        modifiedDataSetListener?.onInserted(relativePosition, rowsToAdd)
    }

    private fun expandFromCache(relativePosition: Int, section: CacheableExpandableSection) {
        section.setForeground()
        val rowsToAdd = section.size - 1
        modifiedDataSetListener?.onInserted(relativePosition, rowsToAdd)
    }


    private fun collapseFromDefault(relativePosition: Int, section: CacheableExpandableSection) {
        val rowsToRemove = section.size - 1
        section.setBackground()
        modifiedDataSetListener?.onRemoved(relativePosition, rowsToRemove)
    }

    private fun collapseFromLoading(relativePosition: Int, section: CacheableExpandableSection) {
        val rowsToRemove = section.size - 1
        section.pause()
        modifiedDataSetListener?.onRemoved(relativePosition, rowsToRemove)
    }

    fun clearCache() {
        sections.forEach { section ->
            if(section is CacheableExpandableSection) {
                (section as CacheableExpandableSection).clearCache()
            }
        }

        modifiedDataSetListener?.onDataSetChange()
    }
}
