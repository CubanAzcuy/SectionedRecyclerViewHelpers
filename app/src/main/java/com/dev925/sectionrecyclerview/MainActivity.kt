package com.dev925.sectionrecyclerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.dev925.sectionrecyclerview.section.CacheableSectionTest
import com.dev925.sectionrecyclerview.section.ExpandableSectionTest
import com.dev925.sectionrecyclerview.section.SectionTest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recycler_view.layoutManager = layoutManager

        val adapter = ExpandableSectionRecyclerViewAdapter()
        recycler_view.adapter = adapter

        adapter.addSection(SectionTest())
        adapter.addSection(ExpandableSectionTest())
        adapter.addSection(CacheableSectionTest())
    }
}
