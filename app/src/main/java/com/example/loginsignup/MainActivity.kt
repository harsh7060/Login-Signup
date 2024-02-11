package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginsignup.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onStart() {
        super.onStart()
        val currentuser : FirebaseUser?= auth.currentUser
        if(currentuser!=null){
            startActivity(Intent(this,Home_Screen::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogle.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.btnSignup.setOnClickListener{
            val intent = Intent(this,Sign_up_activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please Fill all the details!", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,Home_Screen::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"Login Unsuccessful: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount?=task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Login Done", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,Home_Screen::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Failed!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }else{
            Toast.makeText(this,"Faileddd!", Toast.LENGTH_SHORT).show()
        }
    }
}