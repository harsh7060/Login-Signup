package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class Sign_up_activity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //instance of firebase auth
        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener{

             //get text from edit text fields
            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val repeatPass = binding.etRepass.text.toString()


             //checking for any blank fields
            if(email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPass.isEmpty()){
                Toast.makeText(this,"Please Fill all the details!",Toast.LENGTH_SHORT).show()
            }else if(password!=repeatPass){
                Toast.makeText(this,"Both passwords are not same!",Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Registration Successfully",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,Home_Screen::class.java)
                            intent.putExtra("name",username)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"Registration Unsuccessfully! ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}