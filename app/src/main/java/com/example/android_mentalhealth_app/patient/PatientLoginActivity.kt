package com.example.android_mentalhealth_app.patient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_mentalhealth_app.databinding.ActivityLoginBinding
import com.example.android_mentalhealth_app.databinding.ActivityPatientLoginBinding
import com.example.android_mentalhealth_app.patient.ui.theme.Android_mentalhealth_appTheme
import com.google.firebase.auth.FirebaseAuth

class PatientLoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPatientLoginBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPatientLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        binding.signUpHint.setOnClickListener {
            val intent = Intent(this, PatientRegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            loginPatient()
        }
    }

    private fun loginPatient() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(PatientLoginActivity()) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, PatientHomepageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}