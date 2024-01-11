package com.example.android_mentalhealth_app.patient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_mentalhealth_app.databinding.ActivityPatientHomepageBinding
import com.example.android_mentalhealth_app.doctor.models.DoctorData
import com.example.android_mentalhealth_app.patient.adapter.DoctorCustomAdapter
import com.example.android_mentalhealth_app.patient.services.DoctorService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientHomepageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientHomepageBinding
    lateinit var doctorService : DoctorService

    lateinit var patientImage: String
    lateinit var patientName : String

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listView = binding.listView
        doctorService = DoctorService()

        doctorService.getDoctors {
            val adapter = DoctorCustomAdapter(this, it)
            listView.adapter = adapter
        }

        val email = auth.currentUser?.email
        if (email != null){
            db.collection("patients").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        patientImage = documentSnapshot.getString("image")?: ""
                        val patientName = documentSnapshot.getString("first")?: ""
                    }
                }
        }

        listView.setOnItemClickListener(){ adapterView, view, i, l ->
            val selectedItem = adapterView.getItemAtPosition(i) as DoctorData

            val intent = Intent(this, AppointmentActivity::class.java)
            intent.putExtra("name", selectedItem.name)
            intent.putExtra("field", selectedItem.field)
            intent.putExtra("image", selectedItem.image)
            intent.putExtra("email", selectedItem.email)
            intent.putExtra("patientImage", patientImage)
            intent.putExtra("patientName", patientName)
            startActivty(intent)

            true
        }
    }
}