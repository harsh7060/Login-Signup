package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.loginsignup.databinding.ActivityPhotoUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class PhotoUpload : AppCompatActivity() {
    private val binding by lazy {
        ActivityPhotoUploadBinding.inflate(layoutInflater)
    }
    private lateinit var btnUploadImage: Button
    private lateinit var ivShowImage: ImageView

    private fun getFileType(data: Uri): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnUploadImage = binding.btnUploadImage
        ivShowImage = binding.ivViewImage

        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }
    }

    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            if(it.data!=null){
               val ref = Firebase.storage.reference.child("Photo/"+System.currentTimeMillis()+getFileType(it.data!!.data!!))
                ref.putFile(it.data!!.data!!).addOnSuccessListener{
                    ref.downloadUrl.addOnSuccessListener {
                        Firebase.database.reference.child("Photos").push().setValue(it.toString())
                    }
                    Glide.with(this)
                        .load(it)
                        .into(ivShowImage)
                }
            }
        }
    }
}

