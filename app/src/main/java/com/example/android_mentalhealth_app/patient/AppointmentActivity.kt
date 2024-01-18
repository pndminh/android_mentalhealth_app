package com.example.android_mentalhealth_app.patient

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android_mentalhealth_app.databinding.ActivityAppointmentBinding
import com.example.android_mentalhealth_app.doctor.models.AppointmentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.Calendar
import kotlin.math.roundToInt

class AppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var txtName : TextView = binding.doctorName
        var txtField : TextView = binding.doctorField
        var txtEmail : TextView = binding.doctorEmail
        var img : ImageView = binding.doctorImg
        var editSelectTime : EditText = binding.timePicker
        var editSelectDate : EditText = binding.datePicker
        val btnBookAppointment : Button = binding.btnBookAppointment
        var selectedAppointmentDate : String = ""
        var selectedAppointmentTime : String = ""

        val doctorName = intent.getStringExtra("doctorName")
        val doctorEmail = intent.getStringExtra("doctorEmail")
        val doctorImg = intent.getStringExtra("doctorImg")
        val doctorField = intent.getStringExtra("doctorField")
        val patientName = intent.getStringExtra("patientName")
        val patientImg = intent.getStringExtra("patientImg")

        Glide.with(this).load(intent.getStringExtra("doctorImg")).into(img)
        txtName.text = "Dr :" + doctorName
        txtEmail.text = "Email: " + doctorEmail
        txtField.text = "Field: " + doctorField

        val current = Calendar.getInstance()
        val year = current.get(Calendar.YEAR)
        val month = current.get(Calendar.MONTH)
        val dayOfMonth = current.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val current = Calendar.getInstance()
                current.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dayOfWeek = current.get(Calendar.DAY_OF_WEEK)

                if (dayOfWeek == Calendar.SUNDAY) {
                    Toast.makeText(this, "Doctor unavailable, please choose another date", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    var ay = "${selectedMonth + 1}"
                    if (selectedMonth + 1 < 10) {
                        ay = "0${selectedMonth + 1}"
                    }
                    selectedAppointmentDate = "$selectedDayOfMonth.$ay.$selectedYear"
                }
                editSelectDate.setText(selectedAppointmentDate)
            },
            year,
            month,
            dayOfMonth
        )

        val minDate = Calendar.getInstance()
        minDate.add(Calendar.DAY_OF_MONTH, 0)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.DAY_OF_MONTH, 20)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        editSelectDate.setOnClickListener {
            datePickerDialog.show()
        }

        val hour = current.get(Calendar.HOUR_OF_DAY)
        val minute = current.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                // Handle the selected time
                val roundedMinute = ((selectedMinute.toFloat() / 15).roundToInt() * 15) % 60

                if (hour < 9 || hour >= 17) {
                    Toast.makeText(
                        this@AppointmentActivity,
                        "Please select the time in working hour",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    selectedAppointmentTime = String.format("%d:%d", selectedHour, roundedMinute)
                }
                editSelectTime.setText(selectedAppointmentTime)
            },
            hour,
            minute,
            true
        )
        editSelectTime.setOnClickListener {
            timePickerDialog.show()
        }

        btnBookAppointment.setOnClickListener{
            val patientEmail = FirebaseAuth.getInstance().currentUser?.email
            val doctorEmail = intent.getStringExtra("doctorEmail")
            val doctorImg = doctorImg
            val appointmentDate = selectedAppointmentDate
            val appointmentTime = selectedAppointmentTime

            if (patientEmail != null && appointmentDate.isNotEmpty() && appointmentTime.isNotEmpty()) {
                val appointmentInfo = AppointmentData(
                    null,
                    doctorEmail,
                    patientEmail,
                    patientName,
                    doctorName,
                    doctorImg,
                    doctorField,
                    appointmentDate,
                    appointmentTime
                )
                addAppointmentToFireStore(patientEmail, doctorEmail!!, appointmentInfo)
                Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PatientHomepageActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,
                    "Doctor is busy, cannot book appointment", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun addAppointmentToFireStore(
        patientEmail: String,
        doctorEmail: String,
        appointment: AppointmentData
    ){
        val db = FirebaseFirestore.getInstance()

        val patientRef = db.collection("appointments").document(patientEmail)
        val newAppointmentRef = patientRef.collection("patientAppointments").document()

        val doctorRef = db.collection("doctorAppointments").document(doctorEmail)

        val newDoctorAppointmentRef = doctorRef.collection("appointments").document(newAppointmentRef.id)

        newAppointmentRef.set(appointment).addOnSuccessListener {
            newDoctorAppointmentRef.set(appointment)
        }
    }

}