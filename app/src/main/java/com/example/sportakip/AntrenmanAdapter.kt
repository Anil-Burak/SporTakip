package com.example.sportakip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportakip.databinding.AntrenmanRowBinding

class AntrenmanAdapter(
    private val liste: List<AntrenmanTuru>,
    private val onItemClick: (AntrenmanTuru) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<AntrenmanAdapter.TurViewHolder>() {

    inner class TurViewHolder(val binding: AntrenmanRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(liste[position])
                }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(position)
                }
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurViewHolder {
        val binding = AntrenmanRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TurViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TurViewHolder, position: Int) {
        val item = liste[position]
        holder.binding.antrenmanIsim.text = item.isim
        holder.binding.antrenmanIkon.setImageResource(item.ikon)
    }

    override fun getItemCount() = liste.size
}