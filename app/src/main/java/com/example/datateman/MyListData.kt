package com.example.datateman

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.datateman.databinding.ActivityMyListDataBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerViewAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.dataList)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.title = "Data Teman"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText.orEmpty())
                return true
            }
        })
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar ...", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("DataTeman")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataTeman.clear()
                        for (snapshot in dataSnapshot.children) {
                            val teman = snapshot.getValue(data_teman::class.java)
                            teman?.key = snapshot.key
                            dataTeman.add(teman!!)
                        }

                        adapter = RecyclerViewAdapter(dataTeman, this@MyListData)
                        recyclerView?.adapter = adapter
                        adapter?.notifyDataSetChanged()
                        findViewById<TextView>(R.id.noDataText).visibility = if (dataTeman.isEmpty()) View.VISIBLE else View.GONE
                        Toast.makeText(applicationContext, "Data Berhasil dimuat", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    private fun filterData(query: String) {
        val filteredList = dataTeman.filter {
            it.nama?.contains(query, ignoreCase = true) == true ||
                    it.alamat?.contains(query, ignoreCase = true) == true ||
                    it.no_hp?.contains(query, ignoreCase = true) == true
        }
        adapter?.updateList(ArrayList(filteredList))
        findViewById<TextView>(R.id.noDataText).visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }
}
