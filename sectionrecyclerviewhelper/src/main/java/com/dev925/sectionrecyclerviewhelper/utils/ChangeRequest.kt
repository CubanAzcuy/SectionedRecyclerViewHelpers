package com.dev925.sectionrecyclerviewhelper.utils

import com.dev925.sectionrecyclerviewhelper.section.Section

data class ChangeRequest(var section: Section,
                         var action: ChangeType,
                         var startIndex: Int,
                         var range: Int,
                         var shouldBatch: Boolean = false)