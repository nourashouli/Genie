package club.handiman.genie.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import club.handiman.genie.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.widget.Toast
import club.handiman.genie.Utils.Utils
import com.example.genie_cl.adapter.utils.AdapterListener
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success


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




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = ServiceAdapter(context!!, object :AdapterListener{
            override fun onAction(ob: Any) {
                // play ur game here

            }
        })
       // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(context!!, 2)
        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recycler_view.setItemAnimator(DefaultItemAnimator())
        recycler_view.setAdapter(adapter)


        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10),true))
        recycler_view.setItemAnimator(DefaultItemAnimator())




        //TODO
        Fuel.get(Utils.API_Services)
            .header(
                "accept" to "application/json"
              )
            .responseJson { _, _, result ->

                result.success {
//
                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                   //     var services = res.getJSONObject("services")
                        activity!!.runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                adapter.setItem(items.getJSONObject(i))
                            }


                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
               }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }

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
