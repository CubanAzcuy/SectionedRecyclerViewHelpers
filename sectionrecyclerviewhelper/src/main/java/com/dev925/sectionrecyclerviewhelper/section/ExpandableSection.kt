package com.dev925.sectionrecyclerviewhelper.section

open class ExpandableSection: Section() {
    internal var expanded: Boolean = false

    override val size: Int
        get() {
            return if (expanded) {
                super.size
            } else {
                1
            }
        }
}