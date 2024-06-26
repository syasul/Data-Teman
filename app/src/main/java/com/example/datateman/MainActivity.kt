package com.example.datateman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.datateman.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    private lateinit var prodiSpinner: Spinner
    private lateinit var prodiAdapter: ArrayAdapter<String>
    private val prodiList = listOf("S1 - TEKNOLOGI INFORMASI", "S1 - SISTEM INFORMASI", "D3 - SISTEM INFORMASI")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.showData.setOnClickListener(this)
        binding.save.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

        // Initialize Spinner
        prodiSpinner = binding.prodi
        prodiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prodiList)
        prodiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prodiSpinner.adapter = prodiAdapter
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid

                val database = FirebaseDatabase.getInstance()

                val getNama: String = binding.nama.text.toString()
                val getAlamat: String = binding.alamat.text.toString()
                val getNo_hp: String = binding.noHp.text.toString()
                val getProdi: String = prodiSpinner.selectedItem.toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNo_hp)) {
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getNama, getAlamat, getNo_hp, getProdi))
                        .addOnCompleteListener(this) {
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            prodiSpinner.setSelection(0)
                            Toast.makeText(this@MainActivity, "Data tersimpan", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Gagal melakukan logout", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            R.id.showData -> {
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}
