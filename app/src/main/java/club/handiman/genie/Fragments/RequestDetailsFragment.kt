package club.handiman.genie.Fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.adapter.RequestImagesAdapter
import club.handiman.genie.adapter.imagesAdapter
import com.example.genie_cl.R
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.requestdetails.*
import org.json.JSONArray
import org.json.JSONObject


class RequestDetailsFragment(var data: RequestModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.requestdetails, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapter = imagesAdapter(requireContext(), object : AdapterListener {
            override fun onAction(ob: Any) {
                // play ur game here

            }
        })
        // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(requireContext(), 2)
        imagesRecycler.setLayoutManager(mLayoutManager)
        imagesRecycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        imagesRecycler.setItemAnimator(DefaultItemAnimator())
        imagesRecycler.setAdapter(adapter)

        //try

        handymanname1.text = data.handyman
        subject1.text = data.subject
        description1.text = data.request
        date1.text = data.date
        from1.text = data.from
        to1.text = data.to
        val items: JSONArray? = data.images
        for (i in 0 until items!!.length()) {
            adapter!!.setItem(items.get(i))
        }
        //    adapter!!.notifyDataSetChanged()


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