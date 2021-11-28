package com.example.recyclerview

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var inputUser: EditText
    lateinit var submit: Button
    var items = ArrayList<String>()
    private lateinit var lymain: ConstraintLayout
    private lateinit var itemsAapter: RecyclerViewAdapter
    private lateinit var tvphrase: TextView
    private lateinit var tvletter: TextView
    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    private var guessed = 10
    private var phrase = "This is a funny game"
    private var answer = ""
    private var guessedLetters = ""
    private var guessPhrase = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set id
        val Rv = findViewById<RecyclerView>(R.id.rv)
        submit = findViewById(R.id.submit)
        inputUser = findViewById(R.id.tvInputUser)
        lymain = findViewById(R.id.lyMain)
        tvletter = findViewById(R.id.tvLetter)
        tvphrase = findViewById(R.id.tvPhrase)

        for (i in phrase.indices) {
            if (phrase[i] == ' ') {
                myAnswerDictionary[i] = ' '
                answer += ' '
            } else {
                myAnswerDictionary[i] = '*'
                answer += '*'
            }
        }

        tvphrase.text = "Phrase: $answer"
        tvletter.text = "Guessed letter:"
        //set Adapter
        itemsAapter = RecyclerViewAdapter(items)

        // set recycler view adapter
        Rv.layoutManager = LinearLayoutManager(this)
        Rv.adapter = itemsAapter
        submit.setOnClickListener {
            val text = inputUser.text.toString()
            if (!text.isEmpty()) {
                additem()
            } else {
                Snackbar.make(lymain, "Make sure you Enter a String", Snackbar.LENGTH_SHORT).show()
            }
        }
        updateText()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }

    private fun additem() {
        val text = inputUser.text.toString()
        if (guessPhrase) {
            inputUser.clearFocus()
            inputUser.hint = "Guess the full phrase"
            if (text.equals(phrase.toLowerCase())) {
                disableEntry()
                showAlert("You win!! \n Do want play again?")
            } else {
                guessed--
                guessPhrase = false
                items.add("Wrong guess: $text")
                inputUser.text.clear()
                inputUser.clearFocus()
                itemsAapter.notifyDataSetChanged()
                updateText()

            }
        }
            else{
                if (text.isNotEmpty() && text.length == 1) {
                    answer = ""
                    guessPhrase = true
                    findLetter(text.single())
                } else {
                    Snackbar.make(
                        lymain,
                        "Make sure you Enter an Character",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
        inputUser.text.clear()
        inputUser.clearFocus()
        itemsAapter.notifyDataSetChanged()

    }

    private fun updateText() {
        tvphrase.text = "Phrase:  " + answer.toUpperCase()
        tvletter.text = "Guessed Letters:  " + guessedLetters
        if (guessPhrase) {
            inputUser.hint = "Guess the full phrase"
        } else {
            inputUser.hint = "Guess a letter"
        }
    }

    private fun findLetter(letter: Char) {
        var count = 0
        for (i in phrase.indices) {
            if (phrase[i] == letter) {
                myAnswerDictionary[i] = letter
                count++
            }
        }
        for (i in myAnswerDictionary) {
            answer += myAnswerDictionary[i.key]
        }
        inputUser.text.clear()
        inputUser.clearFocus()
        if (guessedLetters.isEmpty()) {
            guessedLetters += letter
        } else {
            guessedLetters += ", " + letter
        }
        if (count > 0) {
            items.add("Found $count ${letter.toUpperCase()}(s)")
        } else {
            items.add("No ${letter.toUpperCase()}s found")
        }

        if (guessed < 10) {
            items.add("$guessed guesses remaining")
        }
        updateText()
        rv.scrollToPosition(items.size - 1)
        updateText()
    }

    private fun disableEntry() {
        submit.isEnabled = false
        submit.isClickable = false
        inputUser.isEnabled = false
        inputUser.isClickable = false
    }

    private fun showAlert(title: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(title)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dia, id ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dia, id ->
                dia.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}