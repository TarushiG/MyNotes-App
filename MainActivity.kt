package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_custom_listitem.view.*
import java.nio.file.Files.delete
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    var createNote:FloatingActionButton?=null
    var listNotes= ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNote=findViewById(R.id.button_add_note)
        createNote?.setOnClickListener {
            val intent = Intent(applicationContext, AddNotes::class.java)
            startActivity(intent)
        }
    }
    fun LoadData(){
        var database=NotesDatabase(this)
        listNotes.clear()
        val cursor=database.query()
        listNotes=cursor
        var myNotesAdapter=MyNotesAdapter(this, listNotes)
        lvNotes.adapter=myNotesAdapter
    }
    override fun onResume(){
        super.onResume()
        LoadData()
        var database=NotesDatabase(this)
        if(database.checkSize()>0){
            noNotes.visibility=View.GONE
            lvNotes.visibility=View.VISIBLE
        }
        else{
            noNotes.visibility=View.VISIBLE
            lvNotes.visibility=View.GONE
        }
    }
    inner class MyNotesAdapter:BaseAdapter{
        var listNotes=ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNotes: ArrayList<Note>):super(){
            this.listNotes=listNotes
            this.context=context
        }
        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(p0: Int): Any {
            return listNotes[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = LayoutInflater.from(parent?.applicationContext).inflate(R.layout.row_custom_listitem,null)
            var myNote=listNotes[p0]
            myView.tvTitle.text= myNote.noteName
            myView.tvDes.text= myNote.noteDes
            myView.ivDelete.setOnClickListener{
                var database= NotesDatabase(context)
                database.delete(myNote.noteId!!)
                LoadData()
                if(database.checkSize()>0){
                    noNotes.visibility=View.GONE
                    lvNotes.visibility=View.VISIBLE
                }
                else{
                    noNotes.visibility=View.VISIBLE
                    lvNotes.visibility=View.GONE
                }
            }
            myView.ivEdit.setOnClickListener{
                GoToUpdate(myNote)
            }
            return myView
        }
    }
    fun GoToUpdate(note: Note){
        var intent= Intent(this, AddNotes::class.java)
        intent.putExtra("ID", note.noteId)
        intent.putExtra("NAME", note.noteName)
        intent.putExtra("DES", note.noteDes)
        startActivity(intent)
    }
}