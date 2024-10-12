package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivityUserDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    var cerbungs:ArrayList<Cerbung> = ArrayList()
    var tempUser:ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarUserDetail)
        supportActionBar?.title = "User Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getIntExtra(Global.USER_ID, 1)
        var currentUser = User()

        with(binding){
            val p = Volley.newRequestQueue(this@UserDetailActivity)
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
                        currentUser = tempUser[0]
                        Log.d("apiresult", cerbungs.toString())

                        val builder = Picasso.Builder(this@UserDetailActivity)
                        builder.listener{picasso, uri, exception -> exception.printStackTrace()}
                        Picasso.get().load(currentUser.image_url).into(binding.imgUserDetail)
                        txtUsernameDetail.text = currentUser.username
                        txtLikeAndTotalCerbung.text = "${currentUser.total_likes} Likes | ${obj.getString("total_cerbungs")} Cerbungs Created"
                        txtLatestPostDetail.text = "Latest Post: None"
                        currentUser.latest_post?.let {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            val date: Date = inputFormat.parse(it)
                            val outputFormat = SimpleDateFormat("dd MMMM yyyy")
                            val formattedDate: String = outputFormat.format(date)
                            txtLatestPostDetail.text = "Latest Post: ${formattedDate}"
                        }
                        txtUsernameCerbungDetail2.text = "${currentUser.username} Cerbung"
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

            val q = Volley.newRequestQueue(this@UserDetailActivity)
            val url = "https://ubaya.me/native/160421109/cerbung/getUserCerbung.php"
            var stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener<String>{
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Cerbung>>() {}.type
                        cerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                        Log.d("apiresult", cerbungs.toString())
                        updateListCerbungUser()

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
            q.add(stringRequest)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
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

    fun updateListCerbungUser() {
        val lm = LinearLayoutManager(this@UserDetailActivity)
        with(binding.recyclerViewDetailUser) {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = UserDetailAdapter(cerbungs)
        }
    }
}