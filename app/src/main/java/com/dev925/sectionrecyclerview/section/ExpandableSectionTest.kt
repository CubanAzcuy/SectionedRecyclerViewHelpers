package com.dev925.sectionrecyclerview.section

import com.dev925.sectionrecyclerview.ExpandableSectionRecyclerViewAdapter
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.section.ExpandableSection

internal class ExpandableSectionTest : ExpandableSection() {
    init {
        sectionHeader = Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.HEADER.ordinal, "Header")
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 1"))
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 2"))
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 3"))
    }
}
