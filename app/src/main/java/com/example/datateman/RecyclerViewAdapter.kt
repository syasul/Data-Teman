package com.example.datateman

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdapter (private var dataTeman: ArrayList<data_teman>, private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val database = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    // ViewHolder class to hold view references
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Nama: TextView = itemView.findViewById(R.id.namax)
        val Alamat: TextView = itemView.findViewById(R.id.alamatx)
        val NoHP: TextView = itemView.findViewById(R.id.no_hpx)
        val ListItem: LinearLayout = itemView.findViewById(R.id.list_item)
    }

    // Update the dataTeman list and notify the adapter
    fun updateList(newList: ArrayList<data_teman>) {
        dataTeman = newList
        notifyDataSetChanged()
    }

    // Create view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_design, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    // Bind data to each view
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val teman = dataTeman[position]
        holder.Nama.text = "Nama: ${teman.nama}"
        holder.Alamat.text = "Alamat: ${teman.alamat}"
        holder.NoHP.text = "NoHP: ${teman.no_hp}"
        holder.ListItem.setOnLongClickListener { view ->
            val actions = arrayOf("Update", "Delete")
            val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
            alert.setItems(actions) { dialog, i ->
                when (i) {
                    0 -> {
                        // Navigate to UpdateData activity
                        val bundle = Bundle()
                        bundle.putString("dataNama", teman.nama)
                        bundle.putString("dataAlamat", teman.alamat)
                        bundle.putString("dataNoHP", teman.no_hp)
                        bundle.putString("getPrimaryKey", teman.key)
                        Log.e("RecyclerViewAdapter", "Navigating to UpdateData activity")
                        val intent = Intent(view.context.applicationContext, UpdateData::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    }
                    1 -> {
                        deleteItem(position)
                    }
                }
            }
            alert.create().show()
            true
        }
    }

    // Get the number of items in the dataTeman list
    override fun getItemCount(): Int {
        return dataTeman.size
    }

    // Delete an item from the dataTeman list and database
    private fun deleteItem(position: Int) {
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.getReference("Admin").child(getUserID).child("DataTeman")

        val deletedItem = dataTeman[position]
        getReference.child(deletedItem.key!!).removeValue()
        dataTeman.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()

        Toast.makeText(context, "Data ${deletedItem.nama} berhasil dihapus", Toast.LENGTH_SHORT).show()
    }
}
