package pl.revolshen.app

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ChildAdapter(private val childList: List<ChildItem>) :
    RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.childLogoIv)
        val title: TextView = itemView.findViewById(R.id.childTitleTv)
        val addressButton: Button = itemView.findViewById(R.id.addressButton)
        val discount: TextView = itemView.findViewById(R.id.discountTv)
        val time: TextView = itemView.findViewById(R.id.timeTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val childItem = childList[position]
        Picasso.get().load(childItem.imageUrl).into(holder.imageView)
        holder.title.text = childItem.title
        holder.addressButton.text = "Pokaż na mapie"
        holder.discount.text = childItem.discount
        holder.time.text = childItem.time

        holder.addressButton.setOnClickListener {
            val address = childItem.address
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return childList.size
    }
}