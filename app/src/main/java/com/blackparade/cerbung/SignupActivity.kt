package com.blackparade.cerbung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ActivitySignupBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            txtSignUpUrl.onFocusChangeListener = View.OnFocusChangeListener{_, hasFocus->
                if(!hasFocus){
                    var url = binding.txtSignUpUrl.text.toString()
                    val builder = Picasso.Builder(this@SignupActivity)

                    builder.listener{picasso, uri, exception->exception.printStackTrace()}
                    builder.build().load(url).into(binding.imgSignUp)
                }
            }

            btnSignUp.setOnClickListener {
                if(TextUtils.isEmpty(txtSignUpUsername.text.toString().trim())){
                    Toast.makeText(it.context, "Username cannot be empty!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if(txtSignUpPassword.text.toString() == txtSignUpConfirm.text.toString()){
                        var newUser = User(0, txtSignUpUsername.text.toString(), txtSignUpPassword.text.toString(), "", 0, "", txtSignUpUrl.text.toString())
                        val q = Volley.newRequestQueue(this@SignupActivity)
                        val url = "https://ubaya.me/native/160421109/cerbung/insertUser.php";
                        val stringRequest = object : StringRequest(
                            Request.Method.POST,
                            url,
                            Response.Listener {
                                Log.d("cekparams", it)
                                val obj = JSONObject(it)
                                if (obj.getString("result") == "OK"){
                                    Toast.makeText(this@SignupActivity, "Account created!", Toast.LENGTH_SHORT).show()
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
                                params["username"] = newUser.username.toString()
                                params["password"] = newUser.password.toString()
                                params["total_likes"] = newUser.total_likes.toString()
                                params["image_url"] = newUser.image_url.toString()
                                return params
                            }
                        }
                        q.add(stringRequest)
                        finish()
                    }
                    else {
                        Toast.makeText(it.context, "Password and Confirm Password don't match!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}