package com.example.mygallery

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import com.example.mygallery.database.MyDatabaseHelper
import com.example.mygallery.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var db: MyDatabaseHelper
    private var hasImage = false
    companion object {
        private val GALARY_CODE = 100
        private val CAM_CODE = 200
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = MyDatabaseHelper(this)
        binding.btnImage.setOnClickListener {
            openGallery()
        }
        binding.btnCamera.setOnClickListener {
            openCam()
        }
        binding.btnSave.setOnClickListener {
            if (hasImage){
                val v = binding.imageView
                val bitmap = (v.drawable as BitmapDrawable).bitmap
                if(db.addImage(bitmap)){
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show()

                }
            }
        }
        binding.btnMove.setOnClickListener {
            startActivity(Intent(this,GalleryActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GALARY_CODE ->{
                if (resultCode == Activity.RESULT_OK){
                    if (data!!.data != null){
                        val uri = data.data
                        binding.imageView.setImageURI(uri)
                        hasImage = true
                    }
                }
            }
            CAM_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    if (data!!.extras != null){
                        val bitmap = data.extras?.get("data") as Bitmap
                        binding.imageView.setImageBitmap(bitmap)
                        hasImage = true
                    }
                }
            }
        }
    }
    private fun openCam(){
        Dexter.withContext(this).withPermission(android.Manifest.permission.CAMERA)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAM_CODE)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "You should accept it!!", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun openGallery() {
        Dexter.withContext(this).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALARY_CODE)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "You should accept it!!", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                   p1!!.continuePermissionRequest()
                }
            }).check()

    }


}
