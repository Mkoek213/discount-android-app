package pl.revolshen.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.revolshen.app.databinding.ParentItemBinding

class ParentAdapter(private val parentItemList : List<ParentItem>): RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding : ParentItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        private fun setChildRecyclerView(childItemList : List<ChildItem>){

        }

        init {
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            binding.card1.setOnClickListener{

                parentItemList[adapterPosition].parentContent1.isOpen = !parentItemList[adapterPosition].parentContent1.isOpen

            }

            binding.card2.setOnClickListener{
                parentItemList[adapterPosition].parentContent2.isOpen = !parentItemList[adapterPosition].parentContent2.isOpen

            }
        }
        fun bind(parentItem : ParentItem)
        {
            binding.parentTv.text = parentItem.parentContent1.title
            binding.parentIv.setImageResource(parentItem.parentContent1.image)

            binding.parent2Tv.text = parentItem.parentContent2.title
            binding.parent2Iv.setImageResource(parentItem.parentContent2.image)

            when(true){
                parentItem.parentContent1.isOpen -> {


                }

                parentItem.parentContent2.isOpen -> {


                }


                else ->{

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ParentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return parentItemList.size
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(parentItemList[position])
    }
}