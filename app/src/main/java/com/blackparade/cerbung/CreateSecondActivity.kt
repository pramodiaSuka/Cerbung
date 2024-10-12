package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.blackparade.cerbung.databinding.ActivityCreateSecondBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class CreateSecondActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateSecondBinding

    companion object{
        val CERBUNG_TITLE = "com.blackparade.cerbung.cerbungTitle"
        val CERBUNG_DESCRIPTION = "com.blackparade.cerbung.cerbungDescription"
        val CERBUNG_URL = "com.blackparade.cerbung.cerbungUrl"
        val CERBUNG_GENRE = "com.blackparade.cerbung.cerbungGenre"
        val CERBUNG_GENREID = "com.blackparade.cerbung.cerbungGenreid"

        val CERBUNG_ACCESS = "com.blackparade.cerbung.cerbungAccess"
        val CERBUNG_PARAGRAPH = "com.blackparade.cerbung.cerbungParagraph"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSecondCreate)
        supportActionBar?.title = "Cerbung Create"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var access = ""
        with(binding){
            btnNextThird.isEnabled = false
            groupAccess.setOnCheckedChangeListener{radioGroup, id ->
                if (id == R.id.radioButtonPublic){
                    access = "public"
                    textInputLayout4.isEnabled = true
                }
                if (id == R.id.radioButtonRestricted){
                    access = "restricted"
                    textInputLayout4.isEnabled = true
                }
            }

            var textWatcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // this function is called before text is edited
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // this function is called when text is edited

                    txtCharCount.text = "${txtInputFirstParagraph.text.toString().length} of 250 characters"
                    if (txtInputFirstParagraph.text.toString() == ""){
                        btnNextThird.isEnabled = false

                    }
                    else {
                        btnNextThird.isEnabled = true
                    }

                }

                override fun afterTextChanged(s: Editable) {
                    // this function is called after text is edited
                }
            }


            txtInputFirstParagraph.addTextChangedListener(textWatcher)
            btnNextThird.setOnClickListener {

                var paragraph = txtInputFirstParagraph.text.toString()

                var title = intent.getStringExtra(CreateFragment.CERBUNG_TITLE)
                var description = intent.getStringExtra(CreateFragment.CERBUNG_DESCRIPTION)
                var genre = intent.getStringExtra(CreateFragment.CERBUNG_GENRE)
                var imgURL = intent.getStringExtra(CreateFragment.CERBUNG_URL)
                var genre_id = intent.getIntExtra(CreateFragment.CERBUNG_GENREID, 1)

                val intent = Intent(it.context, CreateThirdActivity::class.java)

                intent.putExtra(CreateSecondActivity.CERBUNG_TITLE, title)
                intent.putExtra(CreateSecondActivity.CERBUNG_DESCRIPTION, description)
                intent.putExtra(CreateSecondActivity.CERBUNG_URL, imgURL)
                intent.putExtra(CreateSecondActivity.CERBUNG_GENRE, genre)
                intent.putExtra(CreateSecondActivity.CERBUNG_GENREID, genre_id)

                intent.putExtra(CreateSecondActivity.CERBUNG_ACCESS, access)
                intent.putExtra(CreateSecondActivity.CERBUNG_PARAGRAPH, paragraph)
                startActivity(intent)
            }

            btnPrevFirst.setOnClickListener {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.itemNotifToolbar ->
            {
                val intent = Intent(this, NotificationActivity::class.java)
                this.startActivity(intent)
            }
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}