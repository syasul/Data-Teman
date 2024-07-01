package com.example.datateman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.datateman.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHP: String? = null
    private var cekProdi: String? = null
    private lateinit var binding: ActivityUpdateDataBinding

    private lateinit var prodiSpinner: Spinner
    private lateinit var prodiAdapter: ArrayAdapter<String>
    private val prodiList = listOf("S1 - TEKNOLOGI INFORMASI", "S1 - SISTEM INFORMASI", "D3 - SISTEM INFORMASI")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbarx))
        Log.d("UpdateData", "UpdateData activity is launched")
        supportActionBar!!.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize Spinner
        prodiSpinner = binding.newProdi
        prodiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prodiList)
        prodiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prodiSpinner.adapter = prodiAdapter

        data
        binding.update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekNama = binding.newNama.text.toString()
                cekAlamat = binding.newAlamat.text.toString()
                cekNoHP = binding.newNohp.text.toString()
                cekProdi = prodiSpinner.selectedItem.toString()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHP!!) || isEmpty(cekProdi!!)) {
                    Toast.makeText(this@UpdateData, "data tidak boleh kosong", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val setTeman = data_teman()
                    setTeman.nama = binding.newNama.text.toString()
                    setTeman.alamat = binding.newAlamat.text.toString()
                    setTeman.no_hp = binding.newNohp.text.toString()
                    setTeman.prodi = cekProdi
                    updateTeman(setTeman)
                }
            }
        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
        private get() {
            val getNama = intent.extras?.getString("dataNama")
            val getAlamat = intent.extras?.getString("dataAlamat")
            val getNoHP = intent.extras?.getString("dataNoHP")
            val getProdi = intent.extras?.getString("dataProdi")
            binding.newNama.setText(getNama)
            binding.newAlamat.setText(getAlamat)
            binding.newNohp.setText(getNoHP)
            prodiSpinner.setSelection(prodiList.indexOf(getProdi))
        }

    private fun updateTeman(teman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID.toString())
            .child("DataTeman")
            .child(getKey!!)
            .setValue(teman)
            .addOnSuccessListener {
                binding.newNama.setText("")
                binding.newAlamat.setText("")
                binding.newNohp.setText("")
                prodiSpinner.setSelection(0)
                Toast.makeText(this@UpdateData, "Data Berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
