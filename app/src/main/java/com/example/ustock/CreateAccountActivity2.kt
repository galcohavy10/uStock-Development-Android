package com.example.ustock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ustock.CreateAccountActivity2

import android.widget.DatePicker

class CreateAccountActivity2 : ComponentActivity() {

    private lateinit var username: EditText
    private lateinit var birthday: EditText
    private lateinit var password: EditText
    private lateinit var confirm_password: EditText
    private lateinit var buttonCreateAccount: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.`create_account_2`)
        username = findViewById(R.id.editTextUsername)
//        birthDateField = findViewById(R.id.birthday)
        password = findViewById(R.id.password)
        confirm_password = findViewById(R.id.confirm_password)
        buttonCreateAccount = findViewById(R.id.sign_up)
        val datePicker: DatePicker = findViewById(R.id.datePicker)
        buttonCreateAccount.setOnClickListener {
            //Username Database which sees if the user name is already taken

            //Check if passwords match
//            if (password.toString() != confirm_password.toString()){
//                Toast.makeText(this, "The passwords do not match, please try again", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
            //Extract Passwords Here



            //Get the value of the datePicker
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth

            //Extract Dates here





            }
        }
}
