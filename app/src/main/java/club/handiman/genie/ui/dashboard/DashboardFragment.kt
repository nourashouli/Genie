package club.handiman.genie.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.genie_cl.R
import club.handiman.genie.adapter.DashboardPagerAdapter
import kotlinx.android.synthetic.main.fragment_request.*

class DashboardFragment : Fragment() {

    var onGoingFragment = OngoingRequestsFragment()
    var outGoingFragment = OutgoingRequestsFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_request, null, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      var adapter = DashboardPagerAdapter(activity!!.supportFragmentManager)


        adapter.addFragment(onGoingFragment, "On Going")
        adapter.addFragment(outGoingFragment, "Out Going")
//        adapter.addFragment(clientAddressesF, getString(R.string.client_address))
        viewPager!!.offscreenPageLimit = 0
        viewPager!!.adapter = adapter
        tabs!!.setupWithViewPager(viewPager)
        //try
    }


}