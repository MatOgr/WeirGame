package com.example.gralosujaca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var regText: TextView
    private lateinit var logText: TextView
    private lateinit var butRecords: Button
    private lateinit var editNick: EditText
    private lateinit var editPassword: EditText
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(applicationContext)
        editPassword = findViewById<EditText>(R.id.editTextPass)
        editNick = findViewById<EditText>(R.id.editTextNick)
        butRecords = findViewById<Button>(R.id.butRecords)
        logText = findViewById<TextView>(R.id.log_text)
        regText = findViewById<TextView>(R.id.reg_text)
        registerButton = findViewById<Button>(R.id.registerButton)
        loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            login()
        }

        registerButton.setOnClickListener {
            register()
        }

        regText.setOnClickListener {
            regText.visibility = View.INVISIBLE
            logText.visibility = View.VISIBLE
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.VISIBLE
        }

        logText.setOnClickListener {
            logText.visibility = View.INVISIBLE
            regText.visibility = View.VISIBLE
            registerButton.visibility = View.INVISIBLE
            loginButton.visibility = View.VISIBLE
        }

        butRecords.setOnClickListener {
            changeView("")
        }
    }

    private fun login() {
        val nick = editNick.text.toString()
        val passwd = editPassword.text.toString()
        if (nick.isEmpty() || passwd.isEmpty())
            loginResult("")

        else {
            val data = getData(nick, passwd)
            loginResult(data)
        }
    }

    private fun loginResult(data: String?) {
        if (data.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "Couldn\'t log u in with those credentials...",
                Toast.LENGTH_LONG
            ).show()
        }
        else {
            Toast.makeText(
                applicationContext,
                "Successfully logged in",
                Toast.LENGTH_LONG
            ).show()
            changeView(data)
        }
    }

    private fun changeView(data: String?) {
        if (data.isNullOrBlank()) {
            Thread() {
                run {
//                    Thread.sleep(1000)
                    runOnUiThread() {
                        val intent = Intent(this, ListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }.start()
            onPause()
        }
        else {
            Thread() {
                run {
//                    Thread.sleep(1000)
                    Thread() {
                        runOnUiThread() {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("credentials", data)
                            startActivity(intent)
                        }
                    }.start()
                    finish()
                }
            }.start()
        }

    }

    private fun register() {
        val nick = editNick.text.toString()
        val passwd = editPassword.text.toString()
        if (nick.isNotEmpty() && passwd.isNotEmpty()) {
            val data = getData(nick, passwd)
            if (data!!.isEmpty()) {
                dbHelper.addOne(nick, 0, passwd)
                registerResult(getData(nick, passwd))
            }
            else
                registerResult("")
        }
        else
            registerResult("")

    }

    private fun registerResult(data: String?) {
        if(data.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "Couldn\'t register you with provided data...",
                Toast.LENGTH_LONG
            ).show()
        }
        else {
            Toast.makeText(
                applicationContext,
                "Successfully registered",
                Toast.LENGTH_LONG
            ).show()
            changeView(data)
        }
    }

    private fun getData(nick: String?, pass: String?) : String? {

        return dbHelper.getOne(nick, pass)
    }
}