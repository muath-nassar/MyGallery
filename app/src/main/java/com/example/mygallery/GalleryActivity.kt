package com.example.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mygallery.adapter.MyAdapter
import com.example.mygallery.database.MyDatabaseHelper
import com.example.mygallery.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.rcImages.adapter =
            MyAdapter(MyDatabaseHelper(this).getImages())
        binding.rcImages.layoutManager = GridLayoutManager(this,3)
    }
}