package com.example.android_mentalhealth_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android_mentalhealth_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        binding.signUpHint.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            user.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
//                    Next screen
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,
                        it.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this,
                "Please fill in email and password",
                Toast.LENGTH_SHORT).show()
        }
    }
}