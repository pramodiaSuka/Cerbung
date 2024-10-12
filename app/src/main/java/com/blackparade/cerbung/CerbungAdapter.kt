package com.blackparade.cerbung

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackparade.cerbung.databinding.CerbungItemBinding
import com.squareup.picasso.Picasso

class CerbungAdapter(val cerbungs:ArrayList<Cerbung>):RecyclerView.Adapter<CerbungAdapter.CerbungViewHolder>() {
    class CerbungViewHolder(val binding: CerbungItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CerbungViewHolder {
        val binding = CerbungItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CerbungViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: CerbungViewHolder, position: Int) {

        val url = cerbungs[position].cover_image
        with(holder.binding){
            val builder = Picasso.Builder(holder.itemView.context)
            builder.listener{picasso, uri, exception -> exception.printStackTrace()}
            Picasso.get().load(url).into(holder.binding.imgPosterCard)
            txtTitleCard.text = cerbungs[position].title
            txtAuthorCard.text = cerbungs[position].author
            txtParagraphAmount.text = cerbungs[position].total_paragraph.toString()
            txtLikeAmount.text = cerbungs[position].total_likes.toString()
            txtDescriptionCard.text = cerbungs[position].description

            btnReadCard.setOnClickListener {
                val intent = Intent(it.context, ReadAppBarActivity::class.java)
                intent.putExtra(Global.CERBUNG_ID, cerbungs[position].id)
                it.context.startActivity(intent)
            }
        }
    }
}