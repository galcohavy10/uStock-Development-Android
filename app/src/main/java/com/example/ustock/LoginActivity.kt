package com.example.ustock
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext


class LoginActivity: ComponentActivity() {

private lateinit var editTextUsername: EditText
private lateinit var editTextPassword: EditText
private lateinit var buttonLogin: Button


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_layout)

    editTextUsername = findViewById(R.id.editTextUsername)
    editTextPassword = findViewById(R.id.editTextPassword)
    buttonLogin = findViewById(R.id.buttonLogin)


    buttonLogin.setOnClickListener {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()
        val api = API()
        if (username.isNotEmpty() && password.isNotEmpty()) {
            // Perform login logic here
            //TODO: Make a loading icon
            // isLoading.value = true // show the user it's loading

            // this next API call is made in order to allow the user to move around the app with their ID token
            CoroutineScope(Dispatchers.IO).launch {
                val result = api.getUserID(username, password)
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        Log.d("TAG", "User ID is being sent to frontend: ${result.getOrNull()}")
                        try {
                            val json = JSONObject(result.getOrNull())
                            val id = json.optString("userID")
                            if (id.isNotEmpty()) {
                                Log.d("TAG", "Decoded ID is $id")
                                val userPreferences = UserPreferences.getInstance(this@LoginActivity)
                                userPreferences.saveUserID(id) // save it to the user's device so they can stay logged in if they want
                            } else {
                                Log.e("TAG", "Error: could not extract userID from JSON")
                            }
                        } catch (e: JSONException) {
                            Log.e("TAG", "Error: $e")
                        }
                    }
                    else if (result.isFailure) {
                        // handle error, the user needs to see what went wrong
                        Log.e("TAG", "Error: ${result.exceptionOrNull()?.localizedMessage}")
                        Toast.makeText(this@LoginActivity, "Error: Extensive error handling not created yet", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

