package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivityNotificationDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNotificationDetailBinding
    var tempUser:ArrayList<User> = ArrayList()
    var tempCerbung:ArrayList<Cerbung> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarNotifDetail)
        supportActionBar?.title = "Respond"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cerbungId = intent.getIntExtra(Global.CERBUNG_ID, 1)
        val userId = intent.getIntExtra(Global.USER_ID, 1)

        var respondCerbung = Cerbung()
        var respondUser = User()

        with(binding){
            val p = Volley.newRequestQueue(this@NotificationDetailActivity)
            val urlDetail = "https://ubaya.me/native/160421109/cerbung/getDetailedUser.php"
            var stringRequestUser = object : StringRequest(
                Request.Method.POST,
                urlDetail,
                Response.Listener<String>{
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<User>>() {}.type
                        tempUser = Gson().fromJson(data.toString(), sType) as ArrayList<User>
                        respondUser = tempUser[0]
                        Log.d("apiresult", respondCerbung.toString())

                        val builder = Picasso.Builder(this@NotificationDetailActivity)
                        builder.listener{picasso, uri, exception -> exception.printStackTrace()}
                        Picasso.get().load(respondUser.image_url).into(binding.imgDetailNotification)
                        txtDetailUsernameNotif.text = respondUser.username
                        txtLikesAndTotalCerbungDetailNotif.text = "${respondUser.total_likes} Likes | ${obj.getString("total_cerbungs")} Cerbungs Created"
                        txtLatestPostDetailNotif.text = "Latest Post: None"
                        respondUser.latest_post?.let {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            val date: Date = inputFormat.parse(it)
                            val outputFormat = SimpleDateFormat("dd MMMM yyyy")
                            val formattedDate: String = outputFormat.format(date)
                            txtLatestPostDetailNotif.text = "Latest Post: ${formattedDate}"
                        }
                    }
                },
                Response.ErrorListener {
                    Log.e("apiresult", it.message.toString())
                }
            )
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["id"] = userId.toString()
                    return params
                }
            }
            p.add(stringRequestUser)

            val q = Volley.newRequestQueue(this@NotificationDetailActivity)
            val url = "https://ubaya.me/native/160421109/cerbung/getDetailedCerbung.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener{
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Cerbung>>() {}.type
                        tempCerbung = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                        respondCerbung = tempCerbung[0]
                        Log.d("apiresult", respondCerbung.toString())

                        var title = respondCerbung.title
                        var lastUpdated = respondCerbung.last_updated

                        val inputFormatCerbung = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val dateCerbung: Date = inputFormatCerbung.parse(lastUpdated)
                        val outputFormatCerbung = SimpleDateFormat("dd MMMM yyyy")
                        val formattedDateCerbung: String = outputFormatCerbung.format(dateCerbung)

                        var totalLikes = respondCerbung.total_likes.toString()

                        val builderCerbung = Picasso.Builder(this@NotificationDetailActivity)
                        builderCerbung.listener{picasso, uri, exception -> exception.printStackTrace()}
                        Picasso.get().load(respondCerbung.cover_image).into(imgCerbungDetailNotif)
                        txtTitleCerbungDetailNotif.text = title
                        txtLastUpdateCerbungDetailNotif.text = "Last Update: ${formattedDateCerbung}"
                        txtLikeCountCerbungDetailNotif.text = totalLikes
                    }
                },
                Response.ErrorListener{
                    Log.e("apiresult", it.message.toString())
                }
            )
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["id"] = cerbungId.toString()
                    params["users_id"] = Global.currentUser?.id.toString()
                    return params
                }
            }
            q.add(stringRequest)
            
            binding.btnApproveNotif.setOnClickListener {
                val q = Volley.newRequestQueue(this@NotificationDetailActivity)
                val url = "https://ubaya.me/native/160421109/cerbung/insertUserEdit.php";
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK"){
                            Toast.makeText(this@NotificationDetailActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                ){
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["cerbungs_id"] = cerbungId.toString()
                        params["users_id"] = userId.toString()
                        params["action"] = "approve"
                        return params
                    }
                }
                q.add(stringRequest)
            }
            binding.btnDeclineNotif.setOnClickListener {
                val q = Volley.newRequestQueue(this@NotificationDetailActivity)
                val url = "https://ubaya.me/native/160421109/cerbung/insertUserEdit.php";
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    Response.Listener {
                        Log.d("cekparams", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK"){
                            Toast.makeText(this@NotificationDetailActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                ){
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["cerbungs_id"] = cerbungId.toString()
                        params["users_id"] = userId.toString()
                        params["action"] = "decline"
                        return params
                    }
                }
                q.add(stringRequest)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.itemNotifToolbar ->
                Snackbar.make(this, binding.root, "Notification already opened!", Snackbar.LENGTH_SHORT).show()
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}