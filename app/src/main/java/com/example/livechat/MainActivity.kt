package com.example.livechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_register.setOnClickListener{
            performRegister()
        }

        login_textView.setOnClickListener{
            //here login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    fun performRegister(){
        val email = email_edit_register.text.toString()
        val pass = pass_edit_register.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $pass")

        //firebase start from here
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                //else it.isSuccessful
                saveUserToFirebase()
            }
            .addOnFailureListener(){
                Toast.makeText(this, "Fail to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveUserToFirebase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edit_register.text.toString())
        ref.setValue(user)
            .addOnSuccessListener{
                Log.d("RegisterActivity", "finally saved")

                val intent = Intent(this, latest_activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

    }
}
class User(val uid: String, val username: String) {
    constructor() : this("", "")
}
//class User(val uid: String, val username: String)