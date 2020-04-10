package club.handiman.genie.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.genie_cl.R

class OutgoingRequestsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_noti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }


}