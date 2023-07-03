package com.example.ustock

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.widget.Spinner

class CreateAccountActivity : ComponentActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var editTextOption1: EditText
    private lateinit var editTextOption2: EditText
    private lateinit var buttonCreateAccount: Button
    private lateinit var backbutton: Button
    //private val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") // Add more items as needed
    //    var countryCodes = ["+1", "+44", "+91", "+234", "+61", "+880", "+63", "+92", "+20", "+49", "+229", "+593", "+212", "+358", "+33", "+504", "+62", "+964", "+353", "+972", "+39", "+81", "+82", "+60", "+254", "+95", "+98", "+31", "+47", "+966", "+94", "+255", "+66", "+90", "+84", "+56", "+260", "+263", "+965", "+213", "+51", "+7", "+48", "+351", "+91", "+64", "+27", "+65", "+886", "+357", "+34", "+81"]
    private lateinit var dropdown: Spinner
    private val items = arrayOf("🇺🇸 +1", "🇬🇧 +44", "🇮🇳 +91", "🇳🇬 +234", "🇦🇺 +61", "🇧🇩 +880", "🇵🇭 +63", "🇵🇰 +92", "🇪🇬 +20", "🇩🇪 +49", "🇧🇯 +229", "🇪🇨 +593", "🇲🇦 +212", "🇫🇮 +358", "🇫🇷 +33", "🇭🇳 +504", "🇮🇩 +62", "🇮🇶 +964", "🇮🇪 +353", "🇮🇱 +972", "🇮🇹 +39", "🇯🇵 +81", "🇰🇷 +82", "🇲🇾 +60", "🇰🇪 +254", "🇲🇲 +95", "🇮🇷 +98", "🇳🇱 +31", "🇳🇴 +47", "🇸🇦 +966", "🇱🇰 +94", "🇹🇿 +255", "🇹🇭 +66", "🇹🇷 +90", "🇻🇳 +84", "🇨🇱 +56", "🇿🇲 +260", "🇿🇼 +263", "🇰🇼 +965", "🇩🇿 +213", "🇵🇪 +51", "🇷🇺 +7", "🇵🇱 +48", "🇵🇹 +351", "🇮🇳 +91", "🇳🇿 +64", "🇿🇦 +27", "🇸🇬 +65", "🇹🇼 +886", "🇨🇾 +357", "🇪🇸 +34", "🇯🇵 +81")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.`create_account`)

        firstName = findViewById(R.id.editTextFirst)
        lastName = findViewById(R.id.editTextSecond)
        radioGroup = findViewById(R.id.radioGroup1)
        editTextOption1 = findViewById(R.id.editTextOption1)
        editTextOption2 = findViewById(R.id.editTextOption2)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount)


        dropdown = findViewById(R.id.dropdown_phone)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown.adapter = adapter


        //Check when a choice is made
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton1 -> {
                    dropdown.visibility = EditText.GONE
                    editTextOption1.visibility = EditText.VISIBLE
                    editTextOption2.visibility = EditText.GONE
                }
                R.id.radioButton2 -> {
                    dropdown.visibility = EditText.VISIBLE
                    editTextOption1.visibility = EditText.GONE
                    editTextOption2.visibility = EditText.VISIBLE
                }
            }
        }

        buttonCreateAccount.setOnClickListener {
            val first = firstName.text.toString()
            val last = lastName.text.toString()


            //Check if Name is empty
            if (first.isNotEmpty() && last.isNotEmpty()) {
                // go to next page
                val intent = Intent(this, CreateAccountActivity2::class.java)
                startActivity(intent)

                //finish() - will go back to the sign in screen
            } else {
                Toast.makeText(this, "Please enter first and last name", Toast.LENGTH_SHORT).show()
            }
            //Extract Name and Email/Phone Number here




        }
    }


}