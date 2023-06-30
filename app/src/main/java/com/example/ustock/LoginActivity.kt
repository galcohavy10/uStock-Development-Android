package com.example.ustock
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivity: ComponentActivity() {

private lateinit var editTextUsername: EditText
private lateinit var editTextPassword: EditText
private lateinit var buttonLogin: Button
private lateinit var backbutton: Button


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_layout)

    editTextUsername = findViewById(R.id.editTextUsername)
    editTextPassword = findViewById(R.id.editTextPassword)
    buttonLogin = findViewById(R.id.buttonLogin)
    backbutton = findViewById(R.id.button)

    backbutton.setOnClickListener {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    buttonLogin.setOnClickListener {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            // Perform login logic here

            Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
        }
    }
}
}