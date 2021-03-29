package com.example.gralosujaca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        supportActionBar?.hide()

        Thread() {
            run{
                Thread.sleep(3000)
                Thread() {
                    runOnUiThread(){
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }.start()
                finish()
            }
        }.start()

    }
}