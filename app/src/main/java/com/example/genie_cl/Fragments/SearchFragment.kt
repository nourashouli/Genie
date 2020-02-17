package com.example.genie_cl.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.Models.CardView

import com.example.genie_cl.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import android.util.TypedValue
import android.R.attr.top
import android.R.attr.right
import android.R.attr.left
import android.R.attr.bottom
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.R
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.genie_cl.Fragments.SearchFragment.GridSpacingItemDecoration




/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.genie_cl.R.layout.fragment_search, container, false)
    } // onCreateView



    fun getServices(){
        //TODO
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = ServiceAdapter(context!!)
       // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(context!!, 2)
        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recycler_view.setItemAnimator(DefaultItemAnimator())
        recycler_view.setAdapter(adapter)


     //   val mLayoutManager = GridLayoutManager(context!!, 2)
        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10),true))
        recycler_view.setItemAnimator(DefaultItemAnimator())

//        recycler_view.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//
//        recycler_view.adapter = adapter


        adapter.setItem(
            CardView(
                "Plumber",
                "https://handiman.club/public/storage/uploads/dzYci2r374tKkI7NdBtNu3L5K.png",
                3
            )
        )
        adapter.setItem(
                CardView(
                    "electrician",
                    "https://handiman.club/public/storage/uploads/dzYci2r374tKkI7NdBtNu3L5K.png",
                    3
                )
                )
        adapter.setItem(
            CardView(
                "electrician",
                "https://handiman.club/public/storage/uploads/dzYci2r374tKkI7NdBtNu3L5K.png",
                3
            )
        )

    }
    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }


}
