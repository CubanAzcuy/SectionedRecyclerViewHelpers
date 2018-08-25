package com.dev925.sectionrecyclerviewhelper.section

import com.dev925.sectionrecyclerviewhelper.utils.ChangeRequest
import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import java.util.*

class UpdatableSection: Section() {
    internal var updateSectionObservable: UpdatableListener<List<ChangeRequest>>? = null

    protected fun updateSection(changeRequests: ArrayList<ChangeRequest>) {
        updateSectionObservable?.update(changeRequests)
    }
}
