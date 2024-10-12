package com.blackparade.cerbung

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.FragmentCreateBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentCreateBinding
    var genres:ArrayList<Genre> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.me/native/160421109/cerbung/getGenre.php"
        var stringRequest = StringRequest(
            Request.Method.POST,
            url,
            Response.Listener<String>{
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Genre>>() {}.type
                    genres = Gson().fromJson(data.toString(), sType) as ArrayList<Genre>
                    Log.d("apiresult", genres.toString())
                    updateListGenre()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }
        )
        q.add(stringRequest)

//        with(binding){
//            btnNextSecondCerbung.setOnClickListener {
//                var title = txtInputCerbungTitle.text.toString()
//                var description = txtInputCerbungDescription.text.toString()
//                var imgUrl = txtInputCerbungUrl.text.toString()
//                var genre = spinnerGenreCerbung.selectedItem.toString()
//
//                val intent = Intent(it.context, CreateSecondActivity::class.java)
//                intent.putExtra(CreateFragment.CERBUNG_TITLE, title)
//                intent.putExtra(CreateFragment.CERBUNG_DESCRIPTION, description)
//                intent.putExtra(CreateFragment.CERBUNG_URL, imgUrl)
//                intent.putExtra(CreateFragment.CERBUNG_GENRE, genre)
//                startActivity(intent)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_create, container, false)
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnNextSecondCerbung.setOnClickListener {
                var title = txtInputCerbungTitle.text.toString()
                var description = txtInputCerbungDescription.text.toString()
                var imgUrl = txtInputCerbungUrl.text.toString()
                var genre = spinnerGenreCerbung.selectedItem.toString()
                var genreObj = spinnerGenreCerbung.selectedItem as Genre
                var genre_id = genreObj.id

                val intent = Intent(it.context, CreateSecondActivity::class.java)
                intent.putExtra(CreateFragment.CERBUNG_TITLE, title)
                intent.putExtra(CreateFragment.CERBUNG_DESCRIPTION, description)
                intent.putExtra(CreateFragment.CERBUNG_URL, imgUrl)
                intent.putExtra(CreateFragment.CERBUNG_GENRE, genre)
                intent.putExtra(CreateFragment.CERBUNG_GENREID, genre_id)
                startActivity(intent)
            }
        }
    }

    fun updateListGenre(){
        val adapter = ArrayAdapter(requireActivity(), R.layout.myspinner_layout, genres)
        adapter.setDropDownViewResource(R.layout.myspinner_item_layout)
        binding.spinnerGenreCerbung.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        val CERBUNG_TITLE = "com.blackparade.cerbung.cerbungTitle"
        val CERBUNG_DESCRIPTION = "com.blackparade.cerbung.cerbungDescription"
        val CERBUNG_URL = "com.blackparade.cerbung.cerbungUrl"
        val CERBUNG_GENRE = "com.blackparade.cerbung.cerbungGenre"
        val CERBUNG_GENREID = "com.blackparade.cerbung.cerbungGenreid"
    }
}