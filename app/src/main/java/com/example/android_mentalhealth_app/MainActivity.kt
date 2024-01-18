package com.example.android_mentalhealth_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_mentalhealth_app.ui.theme.Android_mentalhealth_appTheme
import com.example.android_mentalhealth_app.patient.PatientLoginActivity

class MainActivity : ComponentActivity() {
    lateinit var patientButton : ImageView
    lateinit var doctorButton : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        patientButton = findViewById(R.id.patientButton)
        doctorButton = findViewById(R.id.doctorButton)

        patientButton.setOnClickListener {
            val intent = Intent(this@MainActivity,PatientLoginActivity::class.java)
            startActivity(intent)

        }

        doctorButton.setOnClickListener {
//            val intent = Intent(this@MainActivity,DoctorLoginActivity::class.java)
//            startActivity(intent)

        }
    }
}

