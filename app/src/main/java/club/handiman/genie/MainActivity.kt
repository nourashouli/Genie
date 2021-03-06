package club.handiman.genie

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import club.handiman.genie.Fragments.ChatLog.RequestsChatFragment
import club.handiman.genie.Fragments.HomeFragment
import club.handiman.genie.Fragments.ProfileFragment
import club.handiman.genie.Fragments.SearchFragment
import club.handiman.genie.ui.dashboard.DashboardFragment
import com.example.genie_cl.R

class MainActivity : AppCompatActivity() {

    private var textMessage: TextView? = null

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    navigateToFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_add_post -> {
                    navigateToFragment(RequestsChatFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_notifications -> {
                    navigateToFragment(DashboardFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    navigateToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }

            false

        } // OnNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        this.textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigateToFragment(HomeFragment())

    } // onCreate

    fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame, fragment)
            .commit()
    }

} // MainActivity