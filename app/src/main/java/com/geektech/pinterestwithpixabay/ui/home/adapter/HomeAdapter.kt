package com.geektech.pinterestwithpixabay.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.geektech.pinterestwithpixabay.databinding.ItemImageBinding
import com.geektech.pinterestwithpixabay.model.HitsModel
import com.geektech.pinterestwithpixabay.model.PixaModel
import okio.utf8Size

class HomeAdapter() :
    androidx.recyclerview.widget.RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private val dataList = arrayListOf<HitsModel>()

    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addData(newData: PixaModel) {
//        dataList.clear()
        dataList.addAll(newData.hits)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class HomeViewHolder(private val binding: ItemImageBinding) : ViewHolder(binding.root) {
        fun bind(model: HitsModel) {
            binding.ivImage.load(model.largeImageURL)
            if (model.tags.length < 16){
                binding.tvTags.text = model.tags}
            else {
                binding.tvTags.text = model.tags.substring(0, 17) + "..."
            }
        }
    }
}