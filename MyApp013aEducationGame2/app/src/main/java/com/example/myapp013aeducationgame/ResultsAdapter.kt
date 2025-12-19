package com.example.myapp013aeducationgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp013aeducationgame.databinding.ResultItemBinding
import java.text.SimpleDateFormat
import java.util.*

class ResultsAdapter : ListAdapter<GameResult, ResultsAdapter.ResultViewHolder>(ResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ResultViewHolder(private val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gameResult: GameResult) {
            binding.playerNameTextView.text = "Player ID: ${gameResult.playerId}" // In a real app, you'd fetch the player's name
            binding.scoreTextView.text = "Score: ${gameResult.score}"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.dateTextView.text = sdf.format(Date(gameResult.createdAt))
        }
    }
}

class ResultDiffCallback : DiffUtil.ItemCallback<GameResult>() {
    override fun areItemsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
        return oldItem == newItem
    }
}
