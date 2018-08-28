package com.dev925.sectionrecyclerviewhelper.section

import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import com.dev925.sectionrecyclerviewhelper.utils.ChangeRequest
import java.util.*

open class UpdatableSection: Section() {
    internal var updateSectionObservable: UpdatableListener<List<ChangeRequest>>? = null

    protected fun updateSection(changeRequests: ArrayList<ChangeRequest>) {
        updateSectionObservable?.update(changeRequests)
    }
}
