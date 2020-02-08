package com.example.genie_cl.Fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Button
import java.util.*

class handymanList : AppCompatActivity() {

    internal lateinit var rv: RecyclerView
    internal lateinit var sortBtn: Button
    internal lateinit var adapter: listAdapter
    private var ascending = true
    companion object {
        private val spacecrafts = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_handyman_list)

        this.initializeViews()
        this.fillSpacecrafts()
    }

    private fun initializeViews() {
        rv = findViewById(R.id.rv)
        rv.setLayoutManager(LinearLayoutManager(this))
        sortBtn = findViewById(R.id.sortBtn)
        sortBtn.setOnClickListener {
            sortData(ascending)
            ascending = !ascending
        }
    }

    private fun sortData(asc: Boolean) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc) {
            spacecrafts.sort()
        } else {
            spacecrafts.reverse()
        }
        adapter = listAdapter(this, spacecrafts)
        rv.adapter = adapter
    }

    private fun fillSpacecrafts() {

        spacecrafts.clear()
        spacecrafts.add("Kepler")
        spacecrafts.add("Casini")
        spacecrafts.add("Voyager")
        spacecrafts.add("New Horizon")
        spacecrafts.add("James Web")
        spacecrafts.add("Apollo 15")
        spacecrafts.add("WMAP")
        spacecrafts.add("Enterprise")
        spacecrafts.add("Spitzer")
        spacecrafts.add("Galileo")
        spacecrafts.add("Challenger")
        spacecrafts.add("Atlantis")
        spacecrafts.add("Apollo 19")
        spacecrafts.add("Huygens")
        spacecrafts.add("Hubble")
        spacecrafts.add("Juno")
        spacecrafts.add("Aries")
        spacecrafts.add("Columbia")

        //ADAPTER
        adapter = listAdapter(this, spacecrafts)
        rv.setAdapter(adapter)
    }

}
