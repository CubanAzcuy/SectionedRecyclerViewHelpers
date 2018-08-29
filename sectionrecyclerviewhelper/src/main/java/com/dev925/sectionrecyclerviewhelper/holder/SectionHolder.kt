package com.dev925.sectionrecyclerviewhelper.holder

import com.dev925.sectionrecyclerviewhelper.listeners.DataSetModifiedListener
import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.section.Section
import com.dev925.sectionrecyclerviewhelper.section.UpdatableSection
import com.dev925.sectionrecyclerviewhelper.utils.ChangeRequest
import com.dev925.sectionrecyclerviewhelper.utils.ChangeType
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

open class SectionHolder(protected var modifiedDataSetListener: DataSetModifiedListener?) : MutableCollection<Section> {

    protected var sections: MutableList<Section> = ArrayList()

    override val size: Int
        get() {
            val totalSize =  sections.sumBy { it.size }
            return totalSize
        }

    protected val iterator: MutableIterator<Section> = object : MutableIterator<Section> {

        private var currentIndex = 0

        override fun hasNext(): Boolean {
            return currentIndex < sections.size
        }

        override fun next(): Section {
            return sections[currentIndex++]
        }

        override fun remove(): Unit {
            //TODO: HERE
        }
    }

    override fun add(element: Section): Boolean {
        val size = size
        val result = sections.add(element)
        if(element is UpdatableSection) {
            (element as UpdatableSection).let { updatableSection ->

                updatableSection.updateSectionObservable = object : UpdatableListener<List<ChangeRequest>> {
                    override fun update(value: List<ChangeRequest>?) {
                        launch(CommonPool) {
                            value?.forEach {
                                resolveChangeRequest(it)
                            }
                        }
                    }
                }
            }
        }

//        if(result) {
//            launch(UI) {
//                modifiedDataSetListener?.onInserted(size, element.size)
//            }
//        }

        return result
    }

    override fun addAll(elements: Collection<Section>): Boolean {
        val size = size
        val result = sections.addAll(elements)
        var addSizes = 0

        launch(CommonPool) {

            elements.forEach { section ->

                addSizes += section.size

                if(section is UpdatableSection) {
                    (section as UpdatableSection).let { updatableSection ->

                        updatableSection.updateSectionObservable = object : UpdatableListener<List<ChangeRequest>> {
                            override fun update(value: List<ChangeRequest>?) {
                                value?.forEach {
                                    launch(CommonPool) {
                                        resolveChangeRequest(it)
                                    }
                                }
                            }
                        }
                    }
                }

//                if (result) {
//                    launch(UI) {
//                        modifiedDataSetListener?.onInserted(size, addSizes)
//                    }
//                }
            }
        }

        return result
    }

    override fun remove(element: Section): Boolean {
        val index = sections.indexOf(element)
        val relativePosition = getRelativePosition(index)
        val result = sections.remove(element)
//
//        if (result) {
//            modifiedDataSetListener?.onRemoved(relativePosition, element.size)
//        }

        return result
    }

    override fun removeAll(elements: Collection<Section>): Boolean {
        elements.forEach {section ->
            val result = remove(section)
            if (!result) return false
        }
        
        return true
    }

    override fun retainAll(elements: Collection<Section>): Boolean {
        sections.forEach { section ->
            if (!elements.contains(section)) {
                val result = remove(section)
                if (!result) return false
            }
        }
        
        return true  
    }

    override fun contains(element: Section): Boolean {
        return sections.contains(element)
    }

    override fun containsAll(elements: Collection<Section>): Boolean {
        return sections.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return sections.isEmpty()
    }

    override fun clear() {
        val size = size
        sections.clear()
        modifiedDataSetListener?.onRemoved(0, size)
    }

    override fun iterator(): MutableIterator<Section> {
        return iterator
    }

    fun getSection(position: Int): Section? {

        var size = 0
        sections.forEach {
            if (size + it.size <= position) {
                size += it.size
            } else {
                return it
            }
        }

        return null
    }

    operator fun get(index: Int): Row? {
        var size = 0
        sections.forEach {
            if (size + it.size <= index) {
                size += it.size
            } else {
                val finalIndex = index - size
                return it[finalIndex]
            }
        }

        return null
    }

    protected fun getRelativePosition(position: Int): Int {
        var relativePosition = 0

        for (i in 0 until position) {
            relativePosition += sections[i].size
        }
        return relativePosition
    }

    fun toArray(): Array<Any> {
        return sections.toTypedArray()
    }

    fun <T> toArray(ts: Array<T>): Array<T> {
        return sections.toTypedArray() as Array<T>
    }


    suspend fun resolveChangeRequest(changeRequest: ChangeRequest): Boolean {
        var relativePositionOfSection = 0

        sections.forEach {
            if(!it.same(changeRequest.section)) {
                relativePositionOfSection += it.size
            } else {
                return@forEach
            }
        }

        launch(UI) {
            when (changeRequest.action) {
                ChangeType.INSERT -> {
                    modifiedDataSetListener?.onInserted(changeRequest.startIndex + relativePositionOfSection, changeRequest.range)
                }
                ChangeType.REMOVE -> {
                    modifiedDataSetListener?.onRemoved(changeRequest.startIndex + relativePositionOfSection, changeRequest.range)
                }
                ChangeType.MOVE -> {
                    modifiedDataSetListener?.onMoved(changeRequest.startIndex + relativePositionOfSection, changeRequest.range)
                }
                ChangeType.CHANGE -> {
                    modifiedDataSetListener?.onChanged(changeRequest.startIndex + relativePositionOfSection, changeRequest.range)
                }
            }
        }

        return true
    }


}
