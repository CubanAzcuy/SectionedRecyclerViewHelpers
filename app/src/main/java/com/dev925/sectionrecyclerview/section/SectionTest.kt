package com.dev925.sectionrecyclerview.section

import com.dev925.sectionrecyclerview.ExpandableSectionRecyclerViewAdapter
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.section.Section

class SectionTest : Section() {
    init {
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Self Body 1"))
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Self Body 2"))
        add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Self Body 3"))
    }

}
