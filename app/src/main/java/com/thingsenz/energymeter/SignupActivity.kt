package com.thingsenz.energymeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private lateinit var email:EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button
    private lateinit var login_link:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth= FirebaseAuth.getInstance()

        email=findViewById<EditText>(R.id.input_email_s)
        password=findViewById<EditText>(R.id.input_password_s)
        signupBtn=findViewById(R.id.btn_signup)
        login_link=findViewById(R.id.link_login)

        signupBtn.setOnClickListener(View.OnClickListener { view ->
            var emText: String=email.text.toString()
            var pwText:String=password.text.toString()

            if (!emText.isEmpty()&&!pwText.isEmpty()) {

                auth.createUserWithEmailAndPassword(emText,pwText).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Signed Up" , Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else{
                        Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                    }
                })

            }
        })



    }
}
