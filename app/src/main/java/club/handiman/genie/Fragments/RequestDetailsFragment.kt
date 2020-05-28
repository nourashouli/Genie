package club.handiman.genie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.adapter.RequestImagesAdapter
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.requestdetails.*
import org.json.JSONObject


class RequestDetailsFragment(var data: Any) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.requestdetails, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //try

      //  handymanname1.text = (data as JSONObject).getString("name")
//        subject1.text = (data as JSONObject).getString("subject")
//        description1.text = (data as JSONObject).getString("description")
//        date1.text = (data as JSONObject).getString("date")
//        from1.text = (data as JSONObject).getString("from")
//        to1.text = (data as JSONObject).getString("to")


                }
            }


