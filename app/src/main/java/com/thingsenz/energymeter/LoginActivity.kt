package com.thingsenz.energymeter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.thingsenz.energymeter.progressbutton.attachTextChangeAnimator
import com.thingsenz.energymeter.progressbutton.bindProgressButton
import com.thingsenz.energymeter.progressbutton.hideProgress
import com.thingsenz.energymeter.progressbutton.showProgress
import kotlinx.android.synthetic.main.activity_login.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {



    private lateinit var auth:FirebaseAuth

    private lateinit var email:EditText
    private lateinit var password: EditText
    //private var loginButton:Button
    private lateinit var signup: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (isOnline(this)) {
        } else {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "No Internet Connection", Snackbar.LENGTH_LONG
            )
                .setAction("RETRY") {
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            snackbar.setActionTextColor(Color.GREEN)
            val sbView = snackbar.view
            val textView =
                sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.YELLOW)
            snackbar.show()
        }

        auth=FirebaseAuth.getInstance()
        email=findViewById(R.id.input_email)
        password=findViewById(R.id.input_password)
        //loginButton=findViewById(R.id.btn_login)
        signup=findViewById(R.id.link_signup)

        bindProgressButton(buttonProgressRightText)
        buttonProgressRightText.attachTextChangeAnimator()
        buttonProgressRightText.setOnClickListener {
            showProgressRight(buttonProgressRightText)
        }


        //loginButton.setOnClickListener {

        //}




    }

    private fun showProgressRight(button: Button) {

        button.showProgress {
            buttonTextRes=R.string.loading
            progressColor=Color.WHITE
        }

        button.isEnabled=false
       /* var emText: String=email.text.toString()
        var pwText:String=password.text.toString()

        if (!emText.isEmpty()&&!pwText.isEmpty()) {

            auth.signInWithEmailAndPassword(emText,pwText).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {*/
                    button.isEnabled=true
                    button.hideProgress(R.string.progressRight)
                    Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
               /* } else{
                    Toast.makeText(this, "Login Failed",Toast.LENGTH_SHORT).show()
                }*/
            //})

        //}


    }

    private fun isOnline(activity: Activity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null) {
            if (networkInfo.isConnectedOrConnecting && ping()) {
                return true
            }
        }
        return false
    }

    private fun ping(): Boolean {
        try {
            val cmd = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(cmd).waitFor() == 0
        } catch (e: InterruptedException) {
            Log.d(SplashActivity.TAG, "Network ping error")
        } catch (e: IOException) {
            Log.d(SplashActivity.TAG, "Network ping error")
        }
        return false
    }

}
