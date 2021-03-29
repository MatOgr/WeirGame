package com.example.gralosujaca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var dbHelper: DBHelper
    private lateinit var addButton: Button
    private lateinit var delButton: Button
    private lateinit var goBackBut: Button
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recycler = findViewById<RecyclerView>(R.id.recyclerRecords)
        dbHelper = DBHelper(applicationContext)
        addButton = findViewById<Button>(R.id.addButton)
        delButton = findViewById<Button>(R.id.delButton)
        goBackBut = findViewById<Button>(R.id.goBack)

        goBackBut.setOnClickListener {
            finish()
        }

        addButton.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "Added record = ${dbHelper.addOne("jan$i", i++, "pass")}",
                Toast.LENGTH_LONG
            ).show()
            showData()
        }

        showData()

//        zostawiłem wyświetlanie haseł dla ułatwienia logowania

//        delButton.setOnClickListener {
//            Toast.makeText(
//                applicationContext,
//                "Deleted record = ${dbHelper.deleteOne("jan0")}",
//                Toast.LENGTH_LONG
//            ).show()
//            showData()
//        }
    }

    fun showData() {
        val list = dbHelper.getAll()

        recycler.adapter = RecyclerAdapter(list)
        recycler.layoutManager = LinearLayoutManager(this)
    }


}