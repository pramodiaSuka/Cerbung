package com.blackparade.cerbung

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.FragmentPrefsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PrefsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrefsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentPrefsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_prefs, container, false)
        binding = FragmentPrefsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            var imageUrl = Global.currentUser?.image_url.toString()
            val builder = Picasso.Builder(requireActivity())
            builder.listener{picasso, uri, exception -> exception.printStackTrace()}
            Picasso.get().load(imageUrl).into(binding.imgProfilePrefs)
            txtUsernamePrefs.setText(Global.currentUser?.username)

            switchDarkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{buttonView, isChecked ->
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

            switchNotifications.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{buttonView, isChecked ->
                if (isChecked){
                    Global.notificationActivated = true
                }
                else {
                    Global.notificationActivated = false
                }
            })

            btnLogOut.setOnClickListener {
                Global.currentUser = null
                val intent = Intent(requireActivity(), SigninActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }

            btnChangePass.setOnClickListener {
                var oldPass = txtPasswordPrefs.text.toString()
                var newPass = txtNewPasswordPrefs.text.toString()
                var rePass = txtRePasswordPrefs.text.toString()
                if ((newPass == rePass) && (newPass != "")){
                    val q = Volley.newRequestQueue(requireActivity())
                    val url = "https://ubaya.me/native/160421109/cerbung/setPasswordUser.php"
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

                                Toast.makeText(requireActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show()
                                txtPasswordPrefs.setText("")
                                txtNewPasswordPrefs.setText("")
                                txtRePasswordPrefs.setText("")
                            }
                            else if (obj.getString("result") == "ERROR"){
                                Toast.makeText(requireActivity(), "Old password incorrect!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener{
                            Log.e("apiresult", it.message.toString())
                            Toast.makeText(requireActivity(), "Old password incorrect!", Toast.LENGTH_SHORT).show()
                        }
                    )
                    {
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["id"] = Global.currentUser?.id.toString()
                            params["password"] = newPass.toString()
                            params["old_password"] = oldPass.toString()
                            return params
                        }
                    }
                    q.add(stringRequest)
                }
                else {
                    Toast.makeText(view.context, "New Password and Retype Password doesn't match!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PrefsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrefsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}