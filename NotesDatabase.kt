package com.example.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DBNAME="MyNotes"
val DBTABLE="Notes"
val COL_ID="ID"
val COL_TITLE="Title"
val COL_DES="Description"
val DB_VERSION=1
var resultList=ArrayList<Note>()
class NotesDatabase:SQLiteOpenHelper {
    var context:Context?=null
    constructor(
        context: Context?,
    ) : super(context, DBNAME, null, DB_VERSION){
        this.context=context
    }

    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        sqliteDatabase!!.execSQL("CREATE TABLE "+ DBTABLE + " (" + COL_ID + "INTEGER PRIMARY KEY," + COL_TITLE + " TEXT" + COL_DES + "TEXT);")
        Toast.makeText(this.context, "Database is Created", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun store(content: ContentValues):Long{
        val db=this.writableDatabase
        val result=db.insert(DBTABLE, null, content)
        db.close()
        return result
    }
    fun query(): ArrayList<Note>{
        val db=this.readableDatabase
        val query="SELECT * FROM "+ DBTABLE
        var cSor=db.rawQuery(query, null)
        if(cSor.moveToFirst())
        {
            do{
                var id=cSor.getInt(cSor.getColumnIndexOrThrow(COL_ID))
                var name=cSor.getString(cSor.getColumnIndexOrThrow(COL_TITLE))
                var des=cSor.getString(cSor.getColumnIndexOrThrow(COL_DES))
                resultList.add(Note(id, name, des))
            }
            while(cSor.moveToNext())
        }
        return resultList
    }

    fun checkSize(): Int{
        var counter=0
        val db=this.readableDatabase
        val query_params="SELECT * FROM "+ DBTABLE
        val cSor=db!!.rawQuery(query_params, null)
        if(cSor.moveToFirst()){
            do{
                counter++
            }while (cSor.moveToNext())
        }
        return counter
    }
    fun delete(id: Int):Int{
        val db=this.writableDatabase
        val count = db!!.delete(DBTABLE, "ID=?", arrayOf(id.toString()))
        db.close()
        return count
    }
    fun update(id:Int, title: String, desc:String):Int{
        val db=this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_TITLE, title)
        cv.put(COL_DES, desc)
        val count=db!!.update(DBTABLE, cv, "ID=?", arrayOf(id.toString()))
        db.close()
        return count
    }
}