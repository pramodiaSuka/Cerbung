package com.blackparade.cerbung

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackparade.cerbung.databinding.UserItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class UserAdapter (val users: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var username = users[position].username.toString()
        var joined_date = users[position].date_joined.toString()
        var image_url = users[position].image_url.toString()
        var totalLikes = users[position].total_likes.toString()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = inputFormat.parse(joined_date)
        val outputFormat = SimpleDateFormat("yyyy")
        val formattedDate: String = outputFormat.format(date)

        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        Picasso.get().load(image_url).into(holder.binding.imgUserItem)

        with(holder.binding){
            txtUsernameItem.text = username
            txtYearJoined.text = "since ${formattedDate}"
            txtTotalLikesItem.text = totalLikes
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, UserDetailActivity::class.java)
            intent.putExtra(Global.USER_ID, users[position].id)
            it.context.startActivity(intent)
        }
    }
}