package com.blackparade.cerbung

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.utils.widget.MockView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivityCreateThirdBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateThirdActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateThirdBinding
    private var newCerbungId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarThirdCreate)
        supportActionBar?.title = "Cerbung Create"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding){
            binding.btnPublish.isEnabled = false
            var displayTitle:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_TITLE).toString()
            var displayDescription:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_DESCRIPTION).toString()
            var displayGenre:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_GENRE).toString()
            var imgUrl:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_URL).toString()
            var genre_id:Int = intent.getIntExtra(CreateSecondActivity.CERBUNG_GENREID, 1)

            var displayAccess:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_ACCESS).toString()
            var displayParagraph:String = intent.getStringExtra(CreateSecondActivity.CERBUNG_PARAGRAPH).toString()


            txtTitleCreateDisplay.text = displayTitle
            txtDescriptionCreateDisplay.text = displayDescription
            txtFirstParagraphCreateDisplay.text = displayParagraph

            chipGenreCreate.text = displayGenre
            if (displayAccess == "restricted"){
                chipRestrictedCreate.visibility = View.VISIBLE
            }
            else {
                chipRestrictedCreate.visibility = View.INVISIBLE
            }

            btnPrevSecond.setOnClickListener {
                finish()
            }

//            bind.txtShowDate1.setOnClickListener{
//                val today = Calendar.getInstance()
//                val year = today.get(Calendar.YEAR)
//                val month = today.get(Calendar.MONTH)
//                val day = today.get(Calendar.DAY_OF_MONTH)
//
//                var picker = DatePickerDialog(this,
//                    DatePickerDialog.OnDateSetListener{ datePicker, selYear, selMonth, selDay ->
//                        val calendar = Calendar.getInstance()
//                        calendar.set(selYear, selMonth, selDay)
//                        var dateFormat = SimpleDateFormat("dd MMMM yyyy")
//                        var str = dateFormat.format(calendar.time)
//                        bind.txtShowDate1.setText(str)
//                    }
//                    , year, month, day)
//                picker.show()
//            }

            btnPublish.setOnClickListener {
                //Get Date to string
                val today = Calendar.getInstance()
                var dateFormat = SimpleDateFormat("yyyy-MM-dd")
                var dateStr = dateFormat.format(today.time)

                //Insert New Cerbung
                val q = Volley.newRequestQueue(this@CreateThirdActivity)
                val url = "https://ubaya.me/native/160421109/cerbung/insertCerbung.php";
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK"){
                            newCerbungId = obj.getString("newCerbungId").toInt()
                            //Insert Notification
                            val urlNotif = "https://ubaya.me/native/160421109/cerbung/insertNotification.php";
                            val stringRequestNotification = object : StringRequest(
                                Request.Method.POST,
                                urlNotif,
                                Response.Listener {
                                    Log.d("cekparams", it)
                                    val obj = JSONObject(it)
                                    if (obj.getString("result") == "OK"){
                                        Log.d("cekparams", obj.getString("message"))
                                        val intent = Intent(this@CreateThirdActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                        finish()
                                    }
                                },
                                Response.ErrorListener {
                                    Log.d("cekparams", it.message.toString())
                                }
                            )
                            {
                                override fun getParams(): MutableMap<String, String>? {
                                    val params = HashMap<String, String>()
                                    params["users_id_sender"] = Global.currentUser?.id.toString()
                                    params["type"] = "news"
                                    params["news_cerbung_id"] = newCerbungId.toString()
                                    return params
                                }
                            }
                            q.add(stringRequestNotification)
                        }
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                )
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["title"] = displayTitle.toString()
                        params["description"] = displayDescription.toString()
                        params["last_updated"] = dateStr.toString()
                        params["edit_access"] = displayAccess.toString()
                        params["genre_id"] = genre_id.toString()
                        params["cover_image"] = imgUrl.toString()
                        params["content"] = displayParagraph.toString()
                        params["users_id"] = Global.currentUser?.id.toString()
                        return params
                    }
                }
                q.add(stringRequest)


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

    fun featuresHandler(v: View){
        if (binding.checkBoxTermPolicies.isChecked){
            binding.btnPublish.isEnabled = true
        }
        else{
            binding.btnPublish.isEnabled = false
        }
    }
}