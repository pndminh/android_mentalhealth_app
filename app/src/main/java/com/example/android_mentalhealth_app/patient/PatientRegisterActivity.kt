package com.example.android_mentalhealth_app.patient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.android_mentalhealth_app.databinding.ActivityPatientRegisterBinding
import com.example.android_mentalhealth_app.patient.PatientLoginActivity
import com.example.android_mentalhealth_app.patient.models.PatientData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginHint.setOnClickListener{
            val intent = Intent(this, PatientLoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener{
            registerUser()
        }

    }
    private fun registerUser(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val name = binding.fullName.text.toString()
        val phone = binding.phone.text.toString()
        val age = binding.age.text.toString()
        val cfPassword = binding.cfPassword.text.toString()
        if (
            email.isNotEmpty()
            && password.isNotEmpty()
            && name.isNotEmpty()
            && phone.isNotEmpty()
            && cfPassword.isNotEmpty()
            && age.isNotEmpty()){
            if(password == cfPassword){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        val client = PatientData(
                            user!!.uid,
                            name,
                            age,
                            email,
                            password,
                            phone,
                        )
                        db.collection("patients").document(user.email!!).set(client)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Client Document successfully written")
                            }
                            .addOnFailureListener{ e ->
                                Log.w("Firestore", "Error writing Client Document", e)
                            }
                        val intent = Intent(this, PatientLoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,
                            it.exception.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this,
                    "Password does not match",
                    Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,
                "Please fill in all fields",
                Toast.LENGTH_SHORT).show()
        }
    }
}