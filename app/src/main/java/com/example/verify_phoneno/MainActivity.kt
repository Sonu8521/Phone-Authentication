package com.example.verify_phoneno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       auth = FirebaseAuth.getInstance()

       var phoneNumberEditText = findViewById<EditText>(R.id.phoneNumberEditText)
       var otpEditText = findViewById<EditText>(R.id.otpEditText)
       var verifyButton = findViewById<Button>(R.id.verifyButton)
       var submitOtpButton = findViewById<Button>(R.id.submitOtpButton)

        verifyButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, java.util.concurrent.TimeUnit.SECONDS, this,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // Handle verification failure
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@MainActivity.verificationId = verificationId
                    }
                })
            Toast.makeText(this@MainActivity, " please wait send OTP", Toast.LENGTH_SHORT).show()

        }


        submitOtpButton.setOnClickListener {

            val otp = otpEditText.text.toString().trim()
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhoneAuthCredential(credential)


        }
                                                                                                
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Phone number authentication successful
                    Toast.makeText(this@MainActivity, "verify Number:", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    // Proceed with logged-in user

                } else {
                    // Phone number authentication failed
                    Toast.makeText(this@MainActivity, "verify field:", Toast.LENGTH_SHORT).show()

                }
                val intent = Intent(this, show::class.java)
                startActivity(intent)
            }
    }
}
