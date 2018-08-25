package com.dev925.sectionrecyclerview.section

import android.os.AsyncTask
import com.dev925.sectionrecyclerview.ExpandableSectionRecyclerViewAdapter
import com.dev925.sectionrecyclerviewhelper.listeners.UpdatableListener
import com.dev925.sectionrecyclerviewhelper.row.Row
import com.dev925.sectionrecyclerviewhelper.section.CacheableExpandableSection
import java.util.concurrent.Callable

class CacheableSectionTest: CacheableExpandableSection() {

    init {
        sectionHeader = Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.HEADER.ordinal, "Header")
        sectionLoadingRow = Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.LOADING.ordinal, "Loading")

    }

    override fun loadData(updatable: UpdatableListener<Any?>) {
        val taskRunner = AsyncTaskRunner()
        taskRunner.execute(Callable<Any?> {
            add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 4"))
            add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 5"))
            add(Row(ExpandableSectionRecyclerViewAdapter.ViewHolders.BODY.ordinal, "Body 6"))
            loadingComplete(updatable)
            null
        })
    }


    private inner class AsyncTaskRunner : AsyncTask<Callable<Any?>, Void, Any?>() {
        internal lateinit var mCallable: Callable<Any?>
        override fun doInBackground(vararg callables: Callable<Any?>): Any? {
            mCallable = callables[0]
            return try {
                Thread.sleep((1000 * 2).toLong())
                null
            } catch (e: InterruptedException) {
                e.printStackTrace()
                null
            }

        }

        override fun onPostExecute(aVoid: Any?) {
            try {
                mCallable.call()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            super.onPostExecute(aVoid)
        }
    }
}
