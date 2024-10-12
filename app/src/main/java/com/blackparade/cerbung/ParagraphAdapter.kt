package com.blackparade.cerbung

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.ParagraphItemBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ParagraphAdapter(val paragraphs:ArrayList<Paragraph>):RecyclerView.Adapter<ParagraphAdapter.ParagraphViewHolder>() {
    class ParagraphViewHolder(val binding:ParagraphItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParagraphViewHolder {
        val binding = ParagraphItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParagraphViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paragraphs.size
    }

    override fun onBindViewHolder(holder: ParagraphViewHolder, position: Int) {
        val content = paragraphs[position].content
        val author = paragraphs[position].author
        val isLiked = paragraphs[position].is_liked
        val totalLikes = paragraphs[position].total_likes
        with(holder.binding){
            txtParagraphContent.text = content
            txtParagraphAuthor.text = author
            checkLikeParagraph.text = totalLikes.toString()
            if(isLiked == 1){
                checkLikeParagraph.isChecked = true
            }
            else if (isLiked == 0){
                checkLikeParagraph.isChecked = false
            }

            checkLikeParagraph.setOnClickListener {
                featuresHandlerLike(it, holder)
            }
        }
    }

    private fun featuresHandlerLike(v: View, holder: ParagraphViewHolder){
        if (holder.binding.checkLikeParagraph.isChecked){
            val q = Volley.newRequestQueue(holder.itemView.context)
            val url = "https://ubaya.me/native/160421109/cerbung/setLikeParagraph.php";
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener {
                    Log.d("cekparams", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        holder.binding.checkLikeParagraph.setText(obj.getString("totalLikes"))
                        Toast.makeText(holder.itemView.context, "Paragraph Liked!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["paragraphs_id"] = paragraphs[holder.adapterPosition].id.toString()
                    params["users_id"] = Global.currentUser?.id.toString()
                    params["action"] = "add"
                    return params
                }
            }
            q.add(stringRequest)
        }
        else{
            val q = Volley.newRequestQueue(holder.itemView.context)
            val url = "https://ubaya.me/native/160421109/cerbung/setLikeParagraph.php";
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                url,
                Response.Listener {
                    Log.d("cekparams", it)
                    val obj = JSONObject(it)
                    if (obj.getString("result") == "OK"){
                        holder.binding.checkLikeParagraph.setText(obj.getString("totalLikes"))
                        Toast.makeText(holder.itemView.context, "Paragraph Unliked!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["paragraphs_id"] = paragraphs[holder.adapterPosition].id.toString()
                    params["users_id"] = Global.currentUser?.id.toString()
                    params["action"] = "delete"
                    return params
                }
            }
            q.add(stringRequest)
        }
    }
}