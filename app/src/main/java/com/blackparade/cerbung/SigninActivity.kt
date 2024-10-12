package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivitySigninBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import kotlin.math.log

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnSignIn.setOnClickListener{
                var currentUser = txtSignInUsername.text.toString()
                var password = txtSignInPassword.text.toString()
                if (password != ""){
                    val q = Volley.newRequestQueue(this@SigninActivity)
                    val url = "https://ubaya.me/native/160421109/cerbung/loginUser.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener{
                            Log.d("apiresult", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){
                                val data = obj.getJSONArray("data")
                                val sType = object : TypeToken<List<User>>() {}.type
                                Global.cerbungUser = Gson().fromJson(data.toString(), sType) as ArrayList<User>
//                                var currentUsername = Global.cerbungUser[0].
                                Global.currentUser = Global.cerbungUser[0]
                                Log.d("apiresult", Global.currentUser.toString())

                                val intent = Intent(this@SigninActivity, MainActivity::class.java)
                                this@SigninActivity.startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this@SigninActivity, "Username or Password are wrong!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener{
                            Log.e("apiresult", it.message.toString())
                            Toast.makeText(this@SigninActivity, "Username or Password are wrong!", Toast.LENGTH_SHORT).show()
                        }
                    )
                    {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
//                        params["id"] = intent.getStringExtra(PlaylistFragment.LATESTID).toString()
                            params["username"] = currentUser.toString()
                            params["password"] = password.toString()
                            return params
                        }
                    }
                    q.add(stringRequest)

                }
            }

            btnRegister.setOnClickListener {
                val intent = Intent(it.context, SignupActivity::class.java)
                it.context.startActivity(intent)
            }
        }
    }
}