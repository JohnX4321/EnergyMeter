package com.thingsenz.energymeter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {


    private lateinit var email: EditText
    private lateinit var resetBtn: Button
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth=FirebaseAuth.getInstance()
        email=findViewById(R.id.email_edt_text)
        resetBtn=findViewById(R.id.reset_pass_btn)

        resetBtn.setOnClickListener { view ->
            val emText=email.text.toString()

            if (!emText.isEmpty()) {
                auth.sendPasswordResetEmail(emText)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }

        }

    }
}
