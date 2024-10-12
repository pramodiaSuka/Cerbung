package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivityReadAppBarBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ReadAppBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadAppBarBinding
    var tempCerbung:ArrayList<Cerbung> = ArrayList()
    var paragraphs:ArrayList<Paragraph> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadAppBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Cerbung Read"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cerbungId = intent.getIntExtra(Global.CERBUNG_ID, 1)
        var currentCerbung = Cerbung()

        with(binding){
            val q = Volley.newRequestQueue(this@ReadAppBarActivity)
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
                        currentCerbung = tempCerbung[0]
                        Log.d("apiresult", Global.currentUser.toString())

                        val builder = Picasso.Builder(this@ReadAppBarActivity)
                        builder.listener{picasso, uri, exception -> exception.printStackTrace()}
                        Picasso.get().load(currentCerbung.cover_image).into(binding.appBarImage)
                        txtTitleReadAppbar.text = currentCerbung.title
                        txtAuthorAppbar.text = "by ${currentCerbung.author}"

                        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date: Date = inputFormat.parse(currentCerbung.last_updated)
                        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
                        val formattedDate: String = outputFormat.format(date)

                        txtDateReadAppbar.text = formattedDate


                        btnTotalParagraph.text = currentCerbung.total_paragraph.toString()
                        checkLikeCerbung.text = currentCerbung.total_likes.toString()
                        chipGenreAppbar.text = currentCerbung.genre

                        if (((currentCerbung.edit_access == "restricted") && (currentCerbung.author != Global.currentUser?.username)) && (currentCerbung.can_be_edited == 0)){
                            chipRestrictedAppbar.visibility = View.VISIBLE
                            btnSubmitAppbar.text = "+ Request to Contribute"
                            txtInputLayoutNewParagraphAppbar.visibility = View.INVISIBLE
                            txtCharCountAppbar.visibility = View.INVISIBLE
                        }
                        else if (((currentCerbung.edit_access == "restricted") && (currentCerbung.author == Global.currentUser?.username)) || (currentCerbung.can_be_edited == 1)){
                            chipRestrictedAppbar.visibility = View.VISIBLE
                            btnSubmitAppbar.text = "Submit"
                            txtInputLayoutNewParagraphAppbar.visibility = View.VISIBLE
                            txtCharCountAppbar.visibility = View.VISIBLE
                        }
                        else {
                            chipRestrictedAppbar.visibility = View.INVISIBLE
                            btnSubmitAppbar.text = "Submit"
                            txtInputLayoutNewParagraphAppbar.visibility = View.VISIBLE
                            txtCharCountAppbar.visibility = View.VISIBLE
                        }

                        if(currentCerbung.is_liked_by_user == 1){
                            checkLikeCerbung.isChecked = true
                        }
                        else if (currentCerbung.is_liked_by_user == 0){
                            checkLikeCerbung.isChecked = false
                        }

                        if (currentCerbung.is_followed_by_user == 1){
                            btnFollowCerbung.setText("Followed")
                        }
                        else if (currentCerbung.is_followed_by_user == 0){
                            btnFollowCerbung.setText("+ Follow")
                        }

                    }
                },
                Response.ErrorListener{
                    Log.e("apiresult", it.message.toString())
                    Toast.makeText(this@ReadAppBarActivity, "Cerbung not found!", Toast.LENGTH_SHORT).show()
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

//          Query Paragraphs
            val p = Volley.newRequestQueue(this@ReadAppBarActivity)
            val urlParagraph = "https://ubaya.me/native/160421109/cerbung/getParagraph.php"
            val stringRequestParagraph = object : StringRequest(
                Request.Method.POST,
                urlParagraph,
                Response.Listener{
                    Log.d("apiresult", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Paragraph>>() {}.type
                        paragraphs = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraph>
                        Log.d("apiresult", Global.currentUser.toString())
                        updateParagraphList()
                    }
                },
                Response.ErrorListener{
                    Log.e("apiresult", it.message.toString())
                    Toast.makeText(this@ReadAppBarActivity, "Paragraph not found!", Toast.LENGTH_SHORT).show()
                }
            )
            {
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["users_id"] = Global.currentUser?.id.toString()
                    params["cerbungs_id"] = cerbungId.toString()
                    return params
                }
            }
            p.add(stringRequestParagraph)
        }
        var textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // this function is called before text is edited
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // this function is called when text is edited

                binding.txtCharCountAppbar.text = "${binding.txtNewParagraphAppbar.text.toString().length} of 250 characters"

            }

            override fun afterTextChanged(s: Editable) {
                // this function is called after text is edited
            }
        }

        binding.txtNewParagraphAppbar.addTextChangedListener(textWatcher)

        with(binding){
            checkLikeCerbung.setOnClickListener {
                featuresHandlerLikeRead(it, currentCerbung)
            }
            btnFollowCerbung.setOnClickListener {
                if (currentCerbung.is_followed_by_user == 0){
                    val q = Volley.newRequestQueue(this@ReadAppBarActivity)
                    val url = "https://ubaya.me/native/160421109/cerbung/setFollowCerbung.php";
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener {
                            Log.d("cekparams", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){
                                currentCerbung.is_followed_by_user = 1
                                binding.btnFollowCerbung.text = "Followed"
                                Toast.makeText(this@ReadAppBarActivity, "Cerbung Followed!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Log.d("cekparams", it.message.toString())
                        }
                    ){
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["cerbungs_id"] = currentCerbung.id.toString()
                            params["users_id"] = Global.currentUser?.id.toString()
                            params["action"] = "add"
                            return params
                        }
                    }
                    q.add(stringRequest)
                }
                else{
                    val q = Volley.newRequestQueue(this@ReadAppBarActivity)
                    val url = "https://ubaya.me/native/160421109/cerbung/setFollowCerbung.php";
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener {
                            Log.d("cekparams", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){
                                currentCerbung.is_followed_by_user = 0
                                binding.btnFollowCerbung.text = "+ Follow"
                                Toast.makeText(this@ReadAppBarActivity, "Cerbung Unfollowed!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Log.d("cekparams", it.message.toString())
                        }
                    ){
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["cerbungs_id"] = currentCerbung.id.toString()
                            params["users_id"] = Global.currentUser?.id.toString()
                            params["action"] = "delete"
                            return params
                        }
                    }
                    q.add(stringRequest)
                }
            }
            btnSubmitAppbar.setOnClickListener {
                if (txtNewParagraphAppbar.text.toString() != ""){
                    var newParagraph = txtNewParagraphAppbar.text.toString()
                    val today = Calendar.getInstance()
                    var dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    var dateStr = dateFormat.format(today.time)
                    var userId = Global.currentUser?.id

                    val q = Volley.newRequestQueue(this@ReadAppBarActivity)
                    val url = "https://ubaya.me/native/160421109/cerbung/insertParagraph.php";
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener {
                            Log.d("cekparams", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){
                                //Temporary storage
                                var newParagraphObj = Paragraph(obj.getString("newParagraphId").toInt(), newParagraph, dateStr, Global.currentUser?.username, 0, 0)
                                paragraphs.add(newParagraphObj)
                                btnTotalParagraph.setText(paragraphs.size.toString())
                                Toast.makeText(this@ReadAppBarActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()
                                updateParagraphList()
                            }
                        },
                        Response.ErrorListener {
                            Log.d("cekparams", it.message.toString())
                        }
                    )
                    {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["content"] = newParagraph.toString()
                            params["upload_date"] = dateStr.toString()
                            params["cerbungs_id"] = cerbungId.toString()
                            params["users_id"] = userId.toString()
                            params["total_paragraph"] = (paragraphs.size + 1).toString()
                            return params
                        }
                    }
                    q.add(stringRequest)
                    txtNewParagraphAppbar.setText("")
                }
                else if (((currentCerbung.edit_access == "restricted") && (currentCerbung.author != Global.currentUser?.username)) && (currentCerbung.can_be_edited == 0)){
                    val q = Volley.newRequestQueue(this@ReadAppBarActivity)
                    val url = "https://ubaya.me/native/160421109/cerbung/insertNotification.php";
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener {
                            Log.d("cekparams", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){
                                Toast.makeText(this@ReadAppBarActivity, "Request Sent!", Toast.LENGTH_SHORT).show()
                                btnSubmitAppbar.text = "Requested"
                                btnSubmitAppbar.isEnabled = false
                            }
                            else if (obj.getString("result") == "ERROR"){
                                Toast.makeText(this@ReadAppBarActivity, "Request Already Sent!", Toast.LENGTH_SHORT).show()
                                btnSubmitAppbar.text = "Requested"
                                btnSubmitAppbar.isEnabled = false
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
                            params["type"] = "request"
                            params["request_cerbung_id"] = cerbungId.toString()
                            params["users_id_receiver"] = currentCerbung.author_id.toString()
                            return params
                        }
                    }
                    q.add(stringRequest)
                }
                else {
                    Toast.makeText(this@ReadAppBarActivity, "Please write a paragraph!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateParagraphList() {
        val lm = LinearLayoutManager(this@ReadAppBarActivity)
        with(binding.recyclerViewAppbar) {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = ParagraphAdapter(paragraphs)
        }
    }

    private fun featuresHandlerLikeRead(v: View, currentCerbung: Cerbung){
        if (binding.checkLikeCerbung.isChecked){
            val q = Volley.newRequestQueue(this@ReadAppBarActivity)
            val url = "https://ubaya.me/native/160421109/cerbung/setLikeCerbung.php";
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener {
                    Log.d("cekparams", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        currentCerbung.total_likes++
                        binding.checkLikeCerbung.text = currentCerbung.total_likes.toString()
                        Toast.makeText(this@ReadAppBarActivity, "Cerbung Liked!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["cerbungs_id"] = currentCerbung.id.toString()
                    params["users_id"] = Global.currentUser?.id.toString()
                    params["author_id"] = currentCerbung.author_id.toString()
                    params["action"] = "add"
                    return params
                }
            }
            q.add(stringRequest)
        }
        else{
            val q = Volley.newRequestQueue(this@ReadAppBarActivity)
            val url = "https://ubaya.me/native/160421109/cerbung/setLikeCerbung.php";
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener {
                    Log.d("cekparams", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        currentCerbung.total_likes--
                        binding.checkLikeCerbung.text = currentCerbung.total_likes.toString()
                        Toast.makeText(this@ReadAppBarActivity, "Cerbung Unliked!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["cerbungs_id"] = currentCerbung.id.toString()
                    params["users_id"] = Global.currentUser?.id.toString()
                    params["author_id"] = currentCerbung.author_id.toString()
                    params["action"] = "delete"
                    return params
                }
            }
            q.add(stringRequest)
        }
    }
}