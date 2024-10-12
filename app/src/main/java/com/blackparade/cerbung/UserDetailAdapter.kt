package com.blackparade.cerbung

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackparade.cerbung.databinding.UserDetailItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class UserDetailAdapter(val cerbungs: ArrayList<Cerbung>) : RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder>() {
    class UserDetailViewHolder(val binding: UserDetailItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailViewHolder {
        val binding = UserDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserDetailAdapter.UserDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: UserDetailViewHolder, position: Int) {
        var title = cerbungs[position].title
        var lastUpdated = cerbungs[position].last_updated

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = inputFormat.parse(lastUpdated)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
        val formattedDate: String = outputFormat.format(date)

        var totalLikes = cerbungs[position].total_likes.toString()

        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener{picasso, uri, exception -> exception.printStackTrace()}
        Picasso.get().load(cerbungs[position].cover_image).into(holder.binding.imgCerbungItemUser)

        with(holder.binding){
            txtCerbungTitleItemUser.text = title
            txtLastUpdateItemUser.text = "Last Update: ${formattedDate}"
            txtLikeCountItemUser.text = totalLikes
        }
    }
}