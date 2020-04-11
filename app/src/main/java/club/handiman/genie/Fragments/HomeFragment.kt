package club.handiman.genie.Fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.MainActivity
import android.content.Intent
import android.widget.Toast
import com.example.genie_cl.R
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.HomeAdapter
import club.handiman.genie.requestForm
import com.example.genie_cl.adapter.utils.AdapterListener
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.runOnUiThread
class HomeFragment : Fragment() {
    var adapter: HomeAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HomeAdapter(context!!)
      getRequest()
       home_recycler_view.adapter = adapter
   }
    fun getRequest() {
        Fuel.get(Utils.API_POST)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        activity!!.runOnUiThread {
                            Toast.makeText(activity, res.toString(), Toast.LENGTH_LONG)
                                .show()
                          val items = res.getJSONArray("post")
                            for (i in 0 until items.length()) {
                                adapter!!.setItem(items.getJSONObject(i))
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
}
// HomeFragment