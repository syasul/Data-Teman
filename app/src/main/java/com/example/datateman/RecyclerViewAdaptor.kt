package com.example.datateman

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class RecyclerViewAdapter (private val dataTeman: ArrayList<data_teman>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val context: Context

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            
        }

}
