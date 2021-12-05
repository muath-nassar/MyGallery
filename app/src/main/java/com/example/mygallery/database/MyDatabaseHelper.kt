package com.example.mygallery.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class MyDatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, NAME,null,VERSION) {
        companion object{
            val NAME = "database"
            val VERSION = 1
        }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT,image BLOB NOT NULL )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS images")
        onCreate(db)
    }
    fun addImage(bitmap : Bitmap): Boolean{
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,outStream)
        val byteArray = outStream.toByteArray()
        val cv = ContentValues()
        cv.put("image",byteArray)
        return writableDatabase.insert("images",null,cv) > 0
    }
    fun getImages():List<Bitmap>{
        val arr = arrayListOf<Bitmap>()
        val cursor : Cursor = readableDatabase.rawQuery("SELECT image FROM images",null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val byteArray = cursor.getBlob(0)
                arr.add(BitmapFactory.decodeByteArray(byteArray,0,byteArray.size))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return arr
    }
    fun delete(id :Int) : Boolean{
        return writableDatabase.delete("images","id = $id",null) > 0
    }
}