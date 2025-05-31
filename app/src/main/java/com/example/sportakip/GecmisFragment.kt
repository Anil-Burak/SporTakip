package com.example.sportakip

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportakip.databinding.FragmentGecmisBinding

class GecmisFragment : Fragment() {

    private var _binding: FragmentGecmisBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: GecmisAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGecmisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GecmisAdapter { index ->
            AlertDialog.Builder(requireContext())
                .setTitle("Silinsin mi?")
                .setMessage("Bu antrenmanı silmek istiyor musun?")
                .setPositiveButton("Evet") { _, _ ->
                    viewModel.antrenmanSil(index)
                }
                .setNegativeButton("Hayır", null)
                .show()
        }

        binding.gecmisRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.gecmisRecyclerView.adapter = adapter

        viewModel.antrenmanListesi.observe(viewLifecycleOwner) { liste ->
            adapter.submitList(liste.toList()) // Kopyasını veriyoruz, diffing için
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}