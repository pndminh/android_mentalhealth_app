package com.example.android_mentalhealth_app.doctor.models

data class AppointmentData (
    val appointmentId: String? = null,
    val doctorEmail : String? = null,
    val patientEmail : String? = null,
    val patientName : String? = null,
    val doctorName : String? = null,
    val doctorImg : String? = null,
    val doctorField : String? = null,
    val date: String? = null,
    val hour : String? = null
)