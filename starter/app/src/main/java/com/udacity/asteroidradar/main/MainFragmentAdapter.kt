package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragmentAdapter(val onClickListener: OnClickListener): ListAdapter<Asteroid, MainFragmentAdapter.AsteroidViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): MainFragmentAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainFragmentAdapter.AsteroidViewHolder, position: Int) {
        val asteroidProperty = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroidProperty)
        }
        holder.bind(asteroidProperty)
    }


    class AsteroidViewHolder (private var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: Asteroid){
            binding.asteroidProperty = asteroid
            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit){
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}

