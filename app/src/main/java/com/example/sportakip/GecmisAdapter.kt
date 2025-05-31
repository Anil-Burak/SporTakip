package com.example.sportakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportakip.databinding.ItemGecmisBinding

class GecmisAdapter(
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<GecmisAdapter.GecmisViewHolder>() {

    private var antrenmanlar: List<AntrenmanVerisi> = emptyList()

    fun submitList(yeniListe: List<AntrenmanVerisi>) {
        antrenmanlar = yeniListe.reversed()
        notifyDataSetChanged()
    }

    inner class GecmisViewHolder(val binding: ItemGecmisBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                onItemLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GecmisViewHolder {
        val binding = ItemGecmisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GecmisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GecmisViewHolder, position: Int) {
        val item = antrenmanlar[position]
        holder.binding.tvTuru.text = item.tur
        holder.binding.tvSure.text = "${item.sureDakika} dakika"
        holder.binding.tvTarih.text = item.tarih
        holder.binding.tvNot.text = "Not: ${item.not}"
    }

    override fun getItemCount() = antrenmanlar.size
}