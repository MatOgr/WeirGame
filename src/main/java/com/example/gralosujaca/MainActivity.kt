package com.example.gralosujaca

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var shotsNum = 0
    private var score = 0
    private var ourRandom = 0
    private var nick = ""
    private var password = ""
    private lateinit var startBut: Button
    private lateinit var guessBut: Button
    private lateinit var scoresBut: Button
    private lateinit var pointsLabel: TextView
    private lateinit var shotsLabel: TextView
    private lateinit var guessLabel: TextView
    private lateinit var editText: EditText
    private lateinit var dbHelper: DBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var data = intent.getStringExtra("credentials")?.split("%@%")
        this.nick = data!![0]
        this.score = data!![1].toInt()
        this.password = data!![2]

        dbHelper = DBHelper(applicationContext)
        editText = findViewById(R.id.editTextNumber)
        shotsLabel = findViewById(R.id.shotsAmountLabel)
        pointsLabel = findViewById(R.id.pointsAmountLabel)
        guessLabel = findViewById(R.id.guessLabel)
        startBut = findViewById(R.id.startButton)
        guessBut = findViewById(R.id.guessButton)
        scoresBut = findViewById(R.id.butScores)

        guessBut.setOnClickListener {
            guess()
        }

        startBut.setOnClickListener {
            startGame()
        }

        scoresBut.setOnClickListener {
            showScores()
        }

        guessLabel.text = "Press START, ${nick} O_o"
        pointsLabel.text = score.toString()
        shotsLabel.text = shotsNum.toString()
        editText.visibility = View.INVISIBLE
    }

    private fun showScores() {
        Thread() {
//            run {
//                Thread.sleep(1000)
                runOnUiThread() {
                    val intent = Intent(
                        this,
                        ListActivity::class.java
                    )
                    startActivity(intent)
                }
//            }
        }.start()
        onPause()
    }

    private fun resetPoints() {
        this.score = 0
        pointsLabel.text = this.score.toString()
        Toast.makeText(
            applicationContext,
            "Your points have been discarded",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun startGame() {
        startBut.visibility = View.INVISIBLE
        guessBut.visibility = View.VISIBLE
        editText.visibility = View.VISIBLE
        guessLabel.text = "Gimme some number"
        ourRandom = Random.nextInt(0, 20)
    }

    private fun guess() {
        val number =  editText.text.toString().toIntOrNull()
        editText.text.clear()
        if(number != null) {
            this.shotsNum++
            when {
                this.shotsNum > 10 -> {
                    guessLabel.text = "You lost, Mate..."
                    endGame()
                }
                number > 20 || number < 0 -> {
                    Toast.makeText(applicationContext, "Number is out of range 0 - 20", Toast.LENGTH_LONG).show()
                    this.shotsNum--
                }
                number == this.ourRandom -> {
                    guessLabel.text = "Yaaaay, U got it!"
                    endGame()
                }
                number > this.ourRandom -> guessLabel.text = "Bit lower, please..."
                else -> guessLabel.text = "Bit higher, please"
            }
            updateLabels()
        }
        else {
            guessLabel.text = "Nothing to shot with"
        }
    }

    private fun endGame() {
        scoring()
        dbHelper.updateData(this.nick, this.password, this.score)
        startBut.visibility = View.VISIBLE
        guessBut.visibility = View.INVISIBLE
    }

    private fun updateLabels() {
        pointsLabel.text = this.score.toString()
        shotsLabel.text = this.shotsNum.toString()
    }

    private fun scoring() {
        var points = 0
        when {
            shotsNum < 2 -> points = 5
            shotsNum < 5 -> points = 3
            shotsNum < 7 -> points = 2
            shotsNum < 11 -> points = 1
        }
        this.score = this.score + points

        val builder = AlertDialog.Builder(this@MainActivity)
        if (points == 0) {
            builder.setTitle("Ultimate Failure...")
            builder.setMessage("Yay! You failed!")
        }
        else {
            builder.setTitle("Victory!")
            builder.setMessage("Yay! You shot $points points in $shotsNum shots!")
        }
        builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->  }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        this.shotsNum = 0

    }
}