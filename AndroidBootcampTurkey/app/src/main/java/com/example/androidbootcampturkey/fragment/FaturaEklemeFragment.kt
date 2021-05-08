package com.example.androidbootcampturkey.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.FragmentFaturaEklemeBinding
import com.example.androidbootcampturkey.model.FaturaData
import com.example.androidbootcampturkey.viewmodel.FaturaViewModel

class FaturaEklemeFragment : Fragment() {
    private lateinit var binding: FragmentFaturaEklemeBinding
    private lateinit var fatura_tipi: String
    private lateinit var para_birimi: String
    private lateinit var fatura_aciklamasi: String
    private lateinit var fatura_tutari: String
    private lateinit var faturaDatabase: FaturaViewModel
    private lateinit var fatura: FaturaData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaturaEklemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.faturaEkleme.setOnClickListener {
            insertToDatabase()
        }
        binding.faturaEklemeYapma.setOnClickListener {
            goFragment(MainFragment())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun goFragment(fragment: Fragment) {
        val frManager = requireActivity().supportFragmentManager
        val transaction = frManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment).commit()
    }

    private fun insertToDatabase() {
        //initialize
        val faturaDetect = binding.faturaTutaritext.text
        val numeric: Boolean = faturaDetect.matches("-?\\d+(\\.\\d+)?".toRegex())

        if (binding.faturaAciklamasitext.text.isNullOrEmpty() || binding.faturaTutaritext.text.isNullOrEmpty() || binding.faturaAciklamasitext.text.isBlank() || binding.faturaTutaritext.text.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Lütfen gerekli alanları doldurunuz",
                Toast.LENGTH_LONG
            ).show()
        } else if (!binding.a1.isChecked && !binding.a2.isChecked && !binding.a3.isChecked) {
            Toast.makeText(requireContext(), "Lütfen fatura tipini seçiniz", Toast.LENGTH_LONG)
                .show()
        } else if (!binding.a4.isChecked && !binding.a5.isChecked && !binding.a6.isChecked && !binding.a7.isChecked) {
            Toast.makeText(requireContext(), "Lütfen para birimini seçiniz", Toast.LENGTH_LONG)
                .show()
        } else if (!numeric) {
            Toast.makeText(
                requireContext(),
                "Lütfen harcama tutarınızı rakamlarla giriniz !!!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            fatura_aciklamasi = binding.faturaAciklamasitext.text.toString()
            fatura_tutari = binding.faturaTutaritext.text.toString()
            //buttons
            when {
                binding.a1.isChecked -> {
                    fatura_tipi = "Fatura"
                }
                binding.a2.isChecked -> {
                    fatura_tipi = "Kira"
                }
                binding.a3.isChecked -> {
                    fatura_tipi = "Diger"
                }
            }
            when {
                binding.a4.isChecked -> {
                    para_birimi = "tl"
                }
                binding.a5.isChecked -> {
                    para_birimi = "dolar"
                }
                binding.a6.isChecked -> {
                    para_birimi = "euro"
                }
                binding.a7.isChecked -> {
                    para_birimi = "sterlin"
                }
            }
            //
            faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
            fatura = FaturaData(fatura_tipi, fatura_aciklamasi, fatura_tutari, para_birimi)
            faturaDatabase.addFatura(fatura)
            Toast.makeText(context, "EKLEME BAŞARILI", Toast.LENGTH_LONG).show()
            goFragment(MainFragment())
        }
    }
}
