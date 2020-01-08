package com.example.livechat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        val email = email_edit_register.text.toString()
        val pass = pass_edit_register.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(){
                Toast.makeText(this, "Fail to create user: ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener(){
                Toast.makeText(this, "Fail to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}