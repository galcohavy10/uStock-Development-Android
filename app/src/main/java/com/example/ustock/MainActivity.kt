package com.example.ustock

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    private lateinit var createAccount: Button
    private lateinit var loginAccount: Button

    //delete eventually
    private lateinit var postShortcut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create Account Button
        createAccount = findViewById(R.id.createAccount)
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        //Login Button
        loginAccount = findViewById(R.id.login)
        loginAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



        //Delete Later
        postShortcut = findViewById(R.id.createPost)
        postShortcut.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
        }



    }
}
