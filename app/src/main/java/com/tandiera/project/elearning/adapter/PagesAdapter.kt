package com.tandiera.project.elearning.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.tandiera.project.elearning.databinding.ItemPageBinding
import com.tandiera.project.elearning.model.Page
import com.tandiera.project.elearning.model.PartsPage

class PagesAdapter(private val context: Context) : PagerAdapter() {

    var pages = mutableListOf<Page>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pageBinding = ItemPageBinding.inflate(LayoutInflater.from(context), container, false)

        bindItem(pageBinding, pages[position])
        container.addView(pageBinding.root)
        return pageBinding.root
    }

    private fun bindItem(pageBinding: ItemPageBinding, page: Page) {
        val partsPageAdapter = PartsPageAdapter()
        pageBinding.rvPage.setHasFixedSize(true)
        partsPageAdapter.partsPage = page.partsPage as MutableList<PartsPage>
        pageBinding.rvPage.adapter = partsPageAdapter
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}