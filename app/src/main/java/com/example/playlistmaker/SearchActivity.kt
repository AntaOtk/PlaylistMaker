package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INPUT_EDIT_TEXT = "INPUT_EDIT_TEXT"
    }

    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButtonIcon)


        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }

            private fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val searchList = findViewById<RecyclerView>(R.id.rvSearch)
        searchList.adapter = trackList
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_EDIT_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(INPUT_EDIT_TEXT).toString()

    }

    val trackList = SearchAdapter(
        listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Goo Goo Muck (Wednesday Addams from \" The Addams Family\") ",
                "Tribute Version Originally Performed By The Cramps, DJ MMT",
                "3:01",
                "https://avatars.yandex.net/get-music-content/6386858/a33a106b.a.24235853-1/200x200"
            ),
            Track(
                "Everybody Jumping! Roman B Static Mix ",
                "Dj Boyko & Sound Shocking, Roman B",
                "2:46",
                "https://avatars.yandex.net/get-music-content/5696151/5e9d9e9a.a.20602270-1/200x200"
            ),
            Track(
                "Supermassive Black Hole",
                "Muse, Matthew Bellamy, Dominic Howard, Chris Wolstenholme",
                "3:32",
                "https://avatars.yandex.net/get-music-content/4304260/b4be538b.a.50197-7/50x50"
            )
        )
    )

}
