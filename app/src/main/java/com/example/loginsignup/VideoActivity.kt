package com.example.loginsignup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.loginsignup.databinding.ActivityVideoBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage

class VideoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }
    private lateinit var mediaController: MediaController
    private lateinit var progreDialog: ProgressDialog

    private fun getFileType(data: Uri): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progreDialog = ProgressDialog(this)

        binding.vvVideo.visibility = View.INVISIBLE
        binding.btnVideoSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            videoLauncher.launch(intent)
        }
    }
    val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                progreDialog.setTitle("Uploading...")
                progreDialog.show()
                val ref = Firebase.storage.reference.child(
                    "Video/" + System.currentTimeMillis() + getFileType(it.data!!.data!!)
                )
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Firebase.database.reference.child("Videos").push().setValue(it.toString())
                        progreDialog.dismiss()
                        Toast.makeText(this,"Video Uploaded ✅",Toast.LENGTH_SHORT).show()
                        binding.btnVideoSelect.visibility = View.INVISIBLE
                        binding.vvVideo.visibility = View.VISIBLE
                        mediaController = MediaController(this)
                        mediaController.setAnchorView(binding.vvVideo)
                        binding.vvVideo.setMediaController(mediaController)
                        binding.vvVideo.setVideoURI(it)
                        binding.vvVideo.start()
                    }
//
                }
                    .addOnProgressListener {
                        val value = (it.bytesTransferred/it.totalByteCount)*100
                        progreDialog.setTitle("Uploading...  $value%")
                    }
            }
        }

    }
}