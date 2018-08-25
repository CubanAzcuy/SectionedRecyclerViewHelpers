package com.dev925.sectionrecyclerviewhelper.section

import com.dev925.sectionrecyclerviewhelper.row.Row

open class Section : MutableList<Row?> {

    var sectionHeader: Row? = null
    protected var sectionRows: MutableList<Row> = ArrayList<Row>()
    protected var sectionFooter: Row? = null

    override val size: Int
        get() {
            var extraRowCount = 0
            if (sectionHeader != null) extraRowCount += 1
            if (sectionFooter != null) extraRowCount += 1
            return sectionRows.size + extraRowCount
        }

    override fun get(index: Int): Row? {
        var headerCount = 0
        if (sectionHeader != null) headerCount = 1

        return if (headerCount == 1 && index == 0) {
            sectionHeader!!
        } else if(sectionFooter != null && index == (headerCount + sectionRows.size)) {
            return  sectionFooter!!
        } else if (sectionRows.isNotEmpty()) {
            sectionRows[index - headerCount]
        } else {
            return null
        }
    }

    //region Helper Accessors
    fun setHeader(type: Int, data: Any?) {
        setHeader(Row(type, data))
    }

    fun setHeader(header: Row) {
        sectionHeader = header
    }

    fun hasHeader(): Boolean {
        return sectionHeader != null
    }

    fun clearHeader() {
        sectionHeader = null
    }

    fun addRow(type: Int, data: Any?) {
        add(Row(type, data))
    }

    fun updateRow(at: Int, type: Int, data: Any?) {
        updateRow(at, Row(type, data))
    }

    fun updateRow(at: Int, row: Row) {
        removeAt(at)
        add(at, row)
    }

    fun removeRow(type: Int, data: Any?) {
        removeRow(Row(type, data))
    }

    fun removeRow(row: Row) {
        remove(row)
    }

    fun insertRow(at: Int, type: Int, data: Any?) {
        add(at, Row(type, data))
    }

    fun setFooter(type: Int, data: Any?) {
        setFooter(Row(type, data))
    }

    fun setFooter(footer: Row) {
        sectionFooter = footer
    }

    fun hasFooter(): Boolean {
        return sectionFooter != null
    }

    fun clearFooter() {
        sectionFooter = null
    }
    //endregion

    //region List Accessors
    override fun add(element: Row?): Boolean {
        return if(element != null) {
            sectionRows.add(element)
        } else {
            false
        }
    }

    override fun add(index: Int, element: Row?) {
        if(element != null) {
            sectionRows.add(index, element)
        }
    }

    override fun addAll(index: Int, elements: Collection<Row?>): Boolean {
        val nonNullRows = elements.filterNotNull()
        return sectionRows.addAll(index, nonNullRows)
    }

    override fun addAll(elements: Collection<Row?>): Boolean {
        val nonNullRows = elements.filterNotNull()
        return sectionRows.addAll(nonNullRows)
    }

    override fun clear() {
        return sectionRows.clear()
    }

    override fun contains(element: Row?): Boolean {
        if(sectionHeader != null && sectionHeader == element) return true
        if(sectionFooter != null && sectionFooter == element) return true
        return sectionRows.contains(element)
    }

    override fun containsAll(elements: Collection<Row?>): Boolean {
        return sectionRows.containsAll(elements)
    }

    override fun indexOf(element: Row?): Int {
        return sectionRows.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        if(sectionHeader != null) return false
        if(sectionFooter != null) return false
        return sectionRows.isEmpty()
    }

    override fun iterator(): MutableIterator<Row> {
        return sectionRows.iterator()
    }

    override fun lastIndexOf(element: Row?): Int {
        return sectionRows.lastIndexOf(element)
    }

    override fun listIterator(): MutableListIterator<Row?> {
        return sectionRows.listIterator() as MutableListIterator<Row?>
    }

    override fun listIterator(index: Int): MutableListIterator<Row?> {
        return sectionRows.listIterator(index) as MutableListIterator<Row?>
    }

    override fun remove(element: Row?): Boolean {
        return sectionRows.remove(element)
    }

    override fun removeAll(elements: Collection<Row?>): Boolean {
        return sectionRows.removeAll(elements)
    }

    override fun removeAt(index: Int): Row {
        return sectionRows.removeAt(index)
    }

    override fun retainAll(elements: Collection<Row?>): Boolean {
        return sectionRows.retainAll(elements)
    }

    override fun set(index: Int, element: Row?): Row? {
        return if(element != null) {
            sectionRows.set(index, element)
        } else {
            null
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Row?> {
        return sectionRows.subList(fromIndex, toIndex) as MutableList<Row?>
    }
    //endregion

    internal suspend fun same(other: Any?): Boolean {
         return other?.let {
            if(it is Section) {
                val otherSection = it as Section
                val headerAreTheSame = rowEquals(sectionHeader, otherSection.sectionHeader)
                val footerAreTheSame = rowEquals(sectionFooter, otherSection.sectionFooter)
                val rowsAreSame = rowsEquals(otherSection)
                headerAreTheSame && footerAreTheSame && rowsAreSame
            } else {
                super.equals(other)
            }
        } ?: run {
            super.equals(other)
        }
    }

    //TODO: Use Custom Thread instead of Common Pool
    //This function test to see if two sections hold the same data
    //To do this it iterates over both collection taking a sample and insuring they are the same
    internal suspend fun rowsEquals(section: Section): Boolean {
        if (sectionRows.size != section.sectionRows.size) {
            return false
        }

        for (i in 0 until sectionRows.size step 5) {
            val myRow = sectionRows[i]
            val otherRow = section.sectionRows[i]

            if (!rowEquals(myRow, otherRow)) {
                return false
            }
        }

        return true
    }

    internal fun rowEquals(myRow: Row?, otherRow: Row?): Boolean {
        if(myRow == null && otherRow == null) {
            return true
        }

        if(myRow == null || otherRow == null) {
            return false
        }

        return myRow == otherRow
    }

}
