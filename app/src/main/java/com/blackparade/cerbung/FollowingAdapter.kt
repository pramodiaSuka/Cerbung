package com.blackparade.cerbung

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blackparade.cerbung.databinding.FollowingItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date


class FollowingAdapter (val cerbungs: ArrayList<Cerbung>): RecyclerView.Adapter<FollowingAdapter.CerbungViewHolder>() {
    class CerbungViewHolder(val binding: FollowingItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CerbungViewHolder {
        val binding = FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CerbungViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: CerbungViewHolder, position: Int) {
        val titleCerbung = cerbungs[position].title
        val authorCerbung = cerbungs[position].author

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = inputFormat.parse(cerbungs[position].last_updated)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
        val formattedDate: String = outputFormat.format(date)

        val url = cerbungs[position].cover_image

        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener{picasso, uri, exception -> exception.printStackTrace()}
        Picasso.get().load(url).into(holder.binding.imgCerbungFollowing)

        with(holder.binding){
            txtTitleFollowing.text = titleCerbung
            txtAuthorFollowing.text = "by ${authorCerbung}"
            txtDateFollowing.text = "Last Update: ${formattedDate}"
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, ReadAppBarActivity::class.java)
            intent.putExtra(Global.CERBUNG_ID, cerbungs[position].id)
            it.context.startActivity(intent)
        }
    }
}