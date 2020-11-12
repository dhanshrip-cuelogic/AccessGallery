package com.example.accessgallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var button : Button
    lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        println("Inside onCreate.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.choose_image_button)
        imageView = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                       READ_PERMISSION_CODE)
               } else {
                   chooseImage()
               }
           } else {
                chooseImage()
           }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            READ_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage()
                } else {
                    Toast.makeText(this,"Couldn't open gallery", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CHOOSE_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data?.data)
        }
    }

    companion object {
        val READ_PERMISSION_CODE = 1
        val CHOOSE_IMAGE_CODE = 2
    }
}