package com.blackparade.cerbung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivityNotificationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    var notifications:ArrayList<Notification> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarNotification)
        supportActionBar?.title = "Notification"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding){
            if(Global.notificationActivated){
                val q = Volley.newRequestQueue(this@NotificationActivity)
                val url = "https://ubaya.me/native/160421109/cerbung/getNotification.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    Response.Listener{
                        Log.d("apiresult", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK"){
                            val data = obj.getJSONArray("data")
                            val sType = object : TypeToken<List<Notification>>() {}.type
                            notifications = Gson().fromJson(data.toString(), sType) as ArrayList<Notification>
                            updateNotificationList()
                        }
                    },
                    Response.ErrorListener{
                        Log.e("apiresult", it.message.toString())
                        Toast.makeText(this@NotificationActivity, "Notification not found!", Toast.LENGTH_SHORT).show()
                    }
                )
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["users_id"] = Global.currentUser?.id.toString()
                        return params
                    }
                }
                q.add(stringRequest)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding){
            if(Global.notificationActivated){
                val q = Volley.newRequestQueue(this@NotificationActivity)
                val url = "https://ubaya.me/native/160421109/cerbung/getNotification.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    url,
                    Response.Listener{
                        Log.d("apiresult", it)
                        val obj = JSONObject(it)
                        if (obj.getString("result") == "OK"){
                            val data = obj.getJSONArray("data")
                            val sType = object : TypeToken<List<Notification>>() {}.type
                            notifications = Gson().fromJson(data.toString(), sType) as ArrayList<Notification>
                            updateNotificationList()
                        }
                    },
                    Response.ErrorListener{
                        Log.e("apiresult", it.message.toString())
                        Toast.makeText(this@NotificationActivity, "Notification not found!", Toast.LENGTH_SHORT).show()
                    }
                )
                {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["users_id"] = Global.currentUser?.id.toString()
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
//        return super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.itemNotifToolbar ->
                Snackbar.make(this, binding.root, "Notification already opened!", Snackbar.LENGTH_SHORT).show()
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    fun updateNotificationList() {
        val lm = LinearLayoutManager(this@NotificationActivity)
        with(binding.recyclerViewNotification) {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = NotificationAdapter(notifications)
        }
    }
}