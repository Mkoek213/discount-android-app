package pl.mikolaj.app

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

class SearchAdapter(private var searchItems: MutableList<SearchItem>)
    : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.childLogoIv)
        val name: TextView = itemView.findViewById(R.id.childTitleTv)
        val addressButton: Button = itemView.findViewById(R.id.addressButton)
        val discount: TextView = itemView.findViewById(R.id.discountTv)
        val time: TextView = itemView.findViewById(R.id.timeTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchItem = searchItems[position]
        Picasso.get().load(searchItem.imageUrl).into(holder.imageView)
        holder.name.text = searchItem.name
        holder.addressButton.text = "Pokaż na mapie"
        holder.discount.text = searchItem.discount
        holder.time.text = searchItem.time

        holder.addressButton.setOnClickListener {
            val address = searchItem.address
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    fun setItems(newItems: List<SearchItem>) {
        searchItems.clear()
        searchItems.addAll(newItems)
        notifyDataSetChanged()
    }

}


