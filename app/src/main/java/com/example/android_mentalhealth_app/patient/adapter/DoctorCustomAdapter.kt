package com.example.android_mentalhealth_app.patient.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.android_mentalhealth_app.R
import com.bumptech.glide.Glide
import com.example.android_mentalhealth_app.doctor.models.DoctorData
import org.w3c.dom.Text

class DoctorCustomAdapter (private val context: Activity, private val list:List<DoctorData>) : ArrayAdapter<DoctorData>(context,
    R.layout.custom_doctor_list, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = context.layoutInflater.inflate(R.layout.custom_doctor_list, null, true)
        val doctorName = rootView.findViewById<TextView>(R.id.doctorName)
        val doctorField = rootView.findViewById<TextView>(R.id.doctorField)
        val doctorEmail = rootView.findViewById<TextView>(R.id.doctorName)
        val doctorImg = rootView.findViewById<ImageView>(R.id.doctorImg)

        val user = list.get(position)
        doctorName.text = user.name
        doctorEmail.text = user.email
        doctorField.text = user.field
        Glide.with(context).load(user.image).into(doctorImg)

        return rootView
    }

}