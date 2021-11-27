package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var inputUser : TextView
    lateinit var submit : Button
     var  items = ArrayList<String>()
    private lateinit var lymain : ConstraintLayout
    private lateinit var itemsAapter : RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set id
        val Rv = findViewById<RecyclerView>(R.id.rv)
        submit = findViewById(R.id.submit)
        inputUser = findViewById(R.id.tvInputUser)
        lymain = findViewById(R.id.lyMain)

        //set Adapter
        itemsAapter =  RecyclerViewAdapter(items)

        // set recycler view adapter
        Rv.layoutManager = LinearLayoutManager(this)
        Rv.adapter = itemsAapter
        submit.setOnClickListener {
            updateinfo()

        }


    }

    private fun updateinfo() {
        val text = inputUser.text.toString()
        if(text.isEmpty()){
            Snackbar.make(lymain,"Please make sure to enter text", Snackbar.LENGTH_SHORT).show()
        }else {
            items.add(text)
            inputUser.text = ""
            itemsAapter.notifyDataSetChanged()
        }
    }
}