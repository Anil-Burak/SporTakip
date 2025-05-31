package com.example.sportakip

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AntrenmanFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AntrenmanAdapter
    private lateinit var antrenmanlar: MutableList<AntrenmanTuru>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_antrenman, container, false)
        recyclerView = view.findViewById(R.id.antrenmanRecyclerView)

        antrenmanlar = mutableListOf(
            AntrenmanTuru("Yüzme", R.drawable.ic_swim),
            AntrenmanTuru("Koşu", R.drawable.ic_run),
            AntrenmanTuru("Bisiklet", R.drawable.ic_bike),
            AntrenmanTuru("Ağırlık", R.drawable.ic_weight),
            AntrenmanTuru("Diğer", R.drawable.ic_more)
        )

        adapter = AntrenmanAdapter(
            antrenmanlar,
            onItemClick = { secilenAntrenman ->
                Toast.makeText(context, "${secilenAntrenman.isim} seçildi", Toast.LENGTH_SHORT).show()

                val fragment = AntrenmanDetayFragment().apply {
                    arguments = Bundle().apply {
                        putString("antrenmanAdi", secilenAntrenman.isim)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onItemLongClick = { index ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Antrenmanı sil")
                    .setMessage("${antrenmanlar[index].isim} silinsin mi?")
                    .setPositiveButton("Evet") { _, _ ->
                        antrenmanlar.removeAt(index)
                        adapter.notifyItemRemoved(index)
                    }
                    .setNegativeButton("Hayır", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }
}