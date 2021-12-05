package com.example.mygallery.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mygallery.R
import com.example.mygallery.databinding.ImageItemBinding
import com.example.mygallery.model.Image

class MyAdapter(val data: List<Image>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    lateinit var context : Context
 class MyViewHolder(val imageItemBinding: ImageItemBinding): RecyclerView.ViewHolder(imageItemBinding.root){

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val img = data[position].image
        holder.imageItemBinding.img.setImageBitmap(img)
        holder.imageItemBinding.img.setOnLongClickListener {
            val popMenu = PopupMenu(context,holder.imageItemBinding.img)
            popMenu.menu.add("delete")
            popMenu.setOnMenuItemClickListener { item ->
                showDeleteAlert()
                true
            }
            true
        }
    }

    private fun showDeleteAlert() {
        AlertDialog.Builder(context).apply {
            setTitle("delete")
            setMessage("Deleted image can't be restored, do you want to delete")
        }
    }

    override fun getItemCount(): Int {
        return  data.size
    }


}