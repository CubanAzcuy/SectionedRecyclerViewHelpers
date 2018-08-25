package com.dev925.sectionrecyclerviewhelper.holder

import com.dev925.sectionrecyclerviewhelper.listeners.DataSetModifiedListener
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.section.ExpandableSection
import com.dev925.sectionrecyclerviewhelper.section.Section
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

open class ExpandableSectionHolder(dataSetModifiedListener: DataSetModifiedListener) : SectionHolder(dataSetModifiedListener) {

    fun selectSectionBy(data: Any?) {
        expandSection(data)
    }

    fun selectSectionBy(index: Int) {
        expandSection(index)
    }

    fun selectSectionBy(row: Row?) {
        expandSection(row)
    }

    fun expandSection(row: Row?) {
        launch(CommonPool) {
        var index = -1

            sections.forEach { section ->
                if (section.sectionHeader == row) {
                    index = section.indexOf(section.sectionHeader)
                    return@forEach
                }
            }

            if (index != -1) {
                expandSection(index)
            }
        }
    }

    fun expandSection(data: Section?) {
        var index = sections.indexOf(data)
        if (index != -1) {
            expandSection(index)
        }
    }

    fun expandSection(data: Any?) {
        launch(CommonPool) {
            var index = -1

            sections.forEach { section ->
                if (section.sectionHeader?.data == data) {
                    index = section.indexOf(section.sectionHeader)
                    return@forEach
                }
            }

            if (index != -1) {
                expandSection(index)
            }
        }

    }

    open fun expandSection(position: Int) {

        if(sections[position] is ExpandableSection) {
            (sections[position] as ExpandableSection).let { expandableSection ->
                val headerOffset = if (expandableSection.hasHeader()) 1 else 0

                if (!expandableSection.expanded) {
                    val relativePosition = getRelativePosition(position) + headerOffset
                    expandableSection.expanded = true

                    val rowsToAdd = expandableSection.size - headerOffset
                    launch(UI) {
                        modifiedDataSetListener?.onInserted(relativePosition, rowsToAdd)
                    }

                } else {
                    collapseSection(position)
                }
            }
        }
    }

    open fun collapseSection(position: Int) {

        if(sections[position] is ExpandableSection) {
            (sections[position] as ExpandableSection).let { expandableSection ->

                val headerOffset = if (expandableSection.hasHeader()) 1 else 0
                if (expandableSection.expanded) {

                    val relativePosition = getRelativePosition(position) + headerOffset
                    val rowsToRemove = expandableSection.size - headerOffset

                    expandableSection.expanded = false
                    launch(UI) {
                        modifiedDataSetListener?.onRemoved(relativePosition, rowsToRemove)
                    }

                } else {
                    expandSection(position)
                }
            }
        }
    }
}
