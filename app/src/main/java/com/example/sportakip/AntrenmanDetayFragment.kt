package com.example.sportakip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sportakip.databinding.FragmentAntrenmanDetayBinding
import androidx.fragment.app.activityViewModels
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.SystemClock

class AntrenmanDetayFragment : Fragment() {

    private var _binding: FragmentAntrenmanDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var antrenmanAdi: String
    private val viewModel: SharedViewModel by activityViewModels()
    private var baslangicZamani: Long = 0
    private var gecenSureDakika: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            antrenmanAdi = it.getString("antrenmanAdi").orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrenmanDetayBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvBaslik.text = "$antrenmanAdi Antrenmanı"

        binding.btnBaslat.setOnClickListener {
            baslangicZamani = SystemClock.elapsedRealtime()
            binding.chronometer.base = baslangicZamani
            binding.chronometer.start()

            binding.btnBaslat.isEnabled = false
            binding.btnBitir.isEnabled = true
            binding.btnKaydet.visibility = View.GONE
            binding.btnKaydet.isEnabled = false
        }

        binding.btnBitir.setOnClickListener {
            val bitisZamani = SystemClock.elapsedRealtime()
            binding.chronometer.stop()

            val gecenSureMs = bitisZamani - baslangicZamani
            gecenSureDakika = (gecenSureMs / 1000 / 60).toInt().coerceAtLeast(1)

            binding.btnBitir.isEnabled = false
            binding.btnKaydet.visibility = View.VISIBLE
            binding.btnKaydet.isEnabled = true
        }

        binding.btnKaydet.setOnClickListener {
            val not = binding.etNot.text.toString()
            val bugun = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

            val veri = AntrenmanVerisi(
                tur = antrenmanAdi,
                sureDakika = gecenSureDakika,
                tarih = bugun,
                not = not
            )

            viewModel.antrenmanEkle(veri)
            Toast.makeText(context, "Antrenman kaydedildi", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}