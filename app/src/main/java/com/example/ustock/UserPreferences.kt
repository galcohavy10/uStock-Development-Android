package com.example.ustock

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context).also { INSTANCE = it }
            }
    }

    fun saveUserID(userID: String) {
        with(sharedPreferences.edit()) {
            putString("userID", userID)
            apply()
        }
    }

    fun getUserID(): String? {
        return sharedPreferences.getString("userID", null)
    }

    fun clear() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
