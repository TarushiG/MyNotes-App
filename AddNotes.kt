package com.example.mynotes

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        try{
            var bundle: Bundle?=intent.extras
            id=bundle!!.getInt("ID", 0)
            if(id!=0){
                etTitle.setText(bundle.getString("NAME"))
                etDes.setText(bundle.getString("DES"))
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
    }
    fun btnAdd(view: View){
        val database= NotesDatabase(this)
        var values= ContentValues()
        if(etTitle.text.length>0){
            values.put("Title", etTitle.text.toString())
            values.put("Description", etDes.text.toString())
        }
        else{
            etTitle.setError("You forgot to add text!")
            etTitle.requestFocus()
            return
        }
        if(id==0){
            val ID=database.store(values)
            if(ID>0){
                Toast.makeText(this, "Note is added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Cannot add note!", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            val ID=database.update(id, etTitle.text.toString(), etDes.text.toString())
            if(ID>0){
                Toast.makeText(this, "Note is added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Cannot add note!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}