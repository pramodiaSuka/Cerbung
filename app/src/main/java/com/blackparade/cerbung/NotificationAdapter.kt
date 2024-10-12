package com.blackparade.cerbung

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blackparade.cerbung.databinding.NotificationItemBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class NotificationAdapter(val notifications: ArrayList<Notification>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    class NotificationViewHolder(val binding: NotificationItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val sentDate = notifications[position].sent_date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = inputFormat.parse(sentDate)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
        val formattedDate: String = outputFormat.format(date)

        val senderUsername = notifications[position].sender_username
        val type = notifications[position].type

        holder.binding.txtSentDateNotif.text = formattedDate

        if (type == "request"){
            holder.binding.txtContentNotif.text = "${senderUsername} Requests to Contributes"
            holder.binding.btnRespondOrView.text = "Respond"
        }
        else if (type == "news"){
            holder.binding.txtContentNotif.text = "${senderUsername} publish new Cerbung"
            holder.binding.btnRespondOrView.text = "View"
        }

        with(holder.binding){
            btnRespondOrView.setOnClickListener {
                if (type == "request"){
                    val intent = Intent(it.context, NotificationDetailActivity::class.java)
                    intent.putExtra(Global.CERBUNG_ID, notifications[position].request_cerbung_id)
                    intent.putExtra(Global.USER_ID, notifications[position].users_id_sender)
                    it.context.startActivity(intent)
                }
                else if (type == "news"){
                    val intent = Intent(it.context, ReadAppBarActivity::class.java)
                    intent.putExtra(Global.CERBUNG_ID, notifications[position].news_cerbung_id)
                    it.context.startActivity(intent)

                    val q = Volley.newRequestQueue(holder.itemView.context)
                    val url = "https://ubaya.me/native/160421109/cerbung/deleteNotification.php";
                    val stringRequest = object : StringRequest(
                        Request.Method.POST,
                        url,
                        Response.Listener {
                            Log.d("cekparams", it)
                            val obj = JSONObject(it)
                            if (obj.getString("result") == "OK"){

                            }
                        },
                        Response.ErrorListener {
                            Log.d("cekparams", it.message.toString())
                        }
                    ){
                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["cerbungs_id"] = notifications[holder.adapterPosition].news_cerbung_id.toString()
                            params["users_id"] = notifications[holder.adapterPosition].users_id_sender.toString()
                            params["receiver_id"] = notifications[holder.adapterPosition].users_id_receiver.toString()
                            return params
                        }
                    }
                    q.add(stringRequest)
                }
            }
        }
    }
}