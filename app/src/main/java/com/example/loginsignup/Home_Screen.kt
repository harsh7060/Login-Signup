package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginsignup.databinding.ActivityHomeScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Home_Screen : AppCompatActivity() {
    private val binding by lazy {
        ActivityHomeScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        binding.btnSignout.setOnClickListener{
            googleSignInClient.signOut().addOnCompleteListener(this){
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            auth.signOut()
//            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
        }

        binding.btnAddNotes.setOnClickListener{
            startActivity(Intent(this,AddNotes::class.java))
        }

        binding.btnOpenNotes.setOnClickListener{
            startActivity(Intent(this,OpenNotes::class.java))
        }
    }


}