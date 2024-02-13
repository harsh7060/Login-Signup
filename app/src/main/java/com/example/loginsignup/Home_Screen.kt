package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
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
    private lateinit var auth: FirebaseAuth
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        tvEmail = binding.tvShowEmail
        tvName = binding.tvName
        ivPhoto = binding.ivPhoto

        binding.btnSignout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener(this) {
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            auth.signOut()
//            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
        }

        binding.btnAddNotes.setOnClickListener {
            startActivity(Intent(this, AddNotes::class.java))
        }

        binding.btnOpenNotes.setOnClickListener {
            startActivity(Intent(this, OpenNotes::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            updateUI(account)
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        tvName.text = account.displayName
        tvEmail.text = account.email

        Glide.with(this)
            .load(account.photoUrl)
            .into(ivPhoto)
    }


}