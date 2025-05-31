package com.example.sportakip

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sportakip.databinding.FragmentProfilBinding
import java.lang.NumberFormatException
import kotlin.math.log10

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private var duzenlemeModu = false
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("antrenman_prefs", Context.MODE_PRIVATE)


        guncelleGorunumuIleVerileriYukle(prefs)

        binding.duzenleButton.setOnClickListener {
            duzenlemeModu = true
            guncelleModGorunumu()
        }

        binding.kaydetButton.setOnClickListener {
            kaydetVerileri(prefs)
        }
        viewModel.antrenmanSayisi.observe(viewLifecycleOwner) { sayi ->
            binding.tvAntrenmanSayisi.text = "Toplam antrenman sayısı: $sayi"
        }
    }

    private fun kaydetVerileri(prefs: android.content.SharedPreferences) {
        try {
            val yeniIsim = binding.etIsim.text.toString()
            val yeniBoy = binding.etBoy.text.toString()
            val yeniKilo = binding.etKilo.text.toString()
            val yeniHedef = binding.etHedef.text.toString()

            val yeniBel = binding.etBel.text.toString()
            val yeniBoyun = binding.etBoyun.text.toString()
            val yeniKalca = binding.etKalca.text.toString()

            val cinsiyet = when {
                binding.rbErkek.isChecked -> "Erkek"
                binding.rbKadin.isChecked -> "Kadın"
                else -> "Erkek"
            }

            // Verileri SharedPreferences'a kaydet
            prefs.edit().apply {
                putString("isim", yeniIsim)
                putString("boy", yeniBoy)
                putString("kilo", yeniKilo)
                putString("hedef", yeniHedef)
                putString("cinsiyet", cinsiyet)
                putString("bel", yeniBel)
                putString("boyun", yeniBoyun)
                putString("kalca", yeniKalca)
                apply()
            }.also {
                duzenlemeModu = false
                guncelleGorunumuIleVerileriYukle(prefs)
                Toast.makeText(requireContext(), "Veriler kaydedildi", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ProfilFragment", "Veri kaydetme hatası: ${e.message}")
            Toast.makeText(
                requireContext(),
                "Veri kaydetme sırasında bir hata oluştu: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun guncelleGorunumuIleVerileriYukle(prefs: android.content.SharedPreferences) {
        val isim = prefs.getString("isim", "") ?: ""
        val boyStr = prefs.getString("boy", "") ?: ""
        val kiloStr = prefs.getString("kilo", "") ?: ""
        val hedef = prefs.getString("hedef", "") ?: ""
        val cinsiyet = prefs.getString("cinsiyet", "Erkek") ?: "Erkek"
        val belStr = prefs.getString("bel", "") ?: ""
        val boyunStr = prefs.getString("boyun", "") ?: ""
        val kalcaStr = prefs.getString("kalca", "") ?: ""

        // Radyo düğmelerini ayarlayın
        binding.rbErkek.isChecked = cinsiyet == "Erkek"
        binding.rbKadin.isChecked = cinsiyet == "Kadın"


        binding.tvIsim.text = "İsim: $isim"
        binding.tvBoy.text = "Boy: $boyStr cm"
        binding.tvKilo.text = "Kilo: $kiloStr kg"
        binding.tvHedef.text = "Hedef: $hedef"


        binding.etIsim.setText(isim)
        binding.etBoy.setText(boyStr)
        binding.etKilo.setText(kiloStr)
        binding.etHedef.setText(hedef)
        binding.etBel.setText(belStr)
        binding.etBoyun.setText(boyunStr)
        binding.etKalca.setText(kalcaStr)

        hesaplaVeGosterYagOrani(prefs)

        guncelleModGorunumu()
    }

    private fun guncelleModGorunumu() {
        val editVisibility = if (duzenlemeModu) View.VISIBLE else View.GONE
        val textVisibility = if (duzenlemeModu) View.GONE else View.VISIBLE

        binding.rgCinsiyet.visibility = editVisibility
        binding.etIsim.visibility = editVisibility
        binding.etBoy.visibility = editVisibility
        binding.etKilo.visibility = editVisibility
        binding.etHedef.visibility = editVisibility
        binding.kaydetButton.visibility = editVisibility
        binding.tvIsim.visibility = textVisibility
        binding.tvBoy.visibility = textVisibility
        binding.tvKilo.visibility = textVisibility
        binding.tvHedef.visibility = textVisibility
        binding.duzenleButton.visibility = textVisibility
        binding.tvYagOrani.visibility = textVisibility
        binding.etBel.visibility = editVisibility
        binding.etBoyun.visibility = editVisibility
        binding.etKalca.visibility = editVisibility
    }

    private fun hesaplaVeGosterYagOrani(prefs: android.content.SharedPreferences) {
        try {
            val boyStr = prefs.getString("boy", "") ?: ""
            val cinsiyet = prefs.getString("cinsiyet", "Erkek") ?: "Erkek"
            val belStr = prefs.getString("bel", "") ?: ""
            val boyunStr = prefs.getString("boyun", "") ?: ""
            val kalcaStr = prefs.getString("kalca", "") ?: ""

            val boy = boyStr.toDoubleOrNull()
            val bel = belStr.toDoubleOrNull()
            val boyun = boyunStr.toDoubleOrNull()
            val kalca = kalcaStr.toDoubleOrNull()

            if (boy != null && bel != null && boyun != null) {
                val yagOrani = hesaplaYagOrani(cinsiyet, boy, bel, boyun, kalca)
                val mesaj = if ((cinsiyet == "Erkek" && yagOrani > 25) || (cinsiyet == "Kadın" && yagOrani > 35)) {
                    " (Yüksek yağ oranı!)"
                } else ""
                binding.tvYagOrani.text = "Yağ Oranı: %.2f%%%s".format(yagOrani, mesaj)
                // Toast.makeText(requireContext(),"${yagOrani}",Toast.LENGTH_SHORT).show()
            } else {
                binding.tvYagOrani.text = "Yağ Oranı: Bilgi Eksik"
            }
        } catch (e: NumberFormatException) {
            binding.tvYagOrani.text = "Yağ Oranı: Hata"
            Log.e("ProfilFragment", "Sayısal format hatası: ${e.message}")

        } catch (e: Exception) {
            binding.tvYagOrani.text = "Yağ Oranı: Hata"
            Log.e("ProfilFragment", "Beklenmeyen hata: ${e.message}")
        }
    }

    private fun hesaplaYagOrani(
        cinsiyet: String,
        boy: Double,
        bel: Double,
        boyun: Double,
        kalca: Double? = null
    ): Double {
        return if (cinsiyet == "Erkek") {
            // Erkeklerde yağ oranı hesaplama formülü
            val yagOrani = 495 / (1.0324 - 0.19077 * log10(bel - boyun) + 0.15456 * log10(boy)) - 450
            yagOrani
        } else {
            // Kadınlarda yağ oranı hesaplama formülü
            if (kalca == null) {
                Log.e("ProfilFragment", "Kadınlar için kalça değeri gerekli.")
                return -1.0
            }
            val yagOrani = 495 / (1.29579 - 0.35004 * log10(bel + kalca - boyun) + 0.22100 * log10(boy)) - 450
            yagOrani
        }
    }
}
