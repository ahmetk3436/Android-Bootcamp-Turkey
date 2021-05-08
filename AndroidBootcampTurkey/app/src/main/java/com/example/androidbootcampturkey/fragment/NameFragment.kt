package com.example.androidbootcampturkey.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.FragmentNameBinding
import com.example.androidbootcampturkey.model.UserName
import com.example.androidbootcampturkey.viewmodel.UserNameViewModel

class NameFragment : Fragment() {
    lateinit var binding: FragmentNameBinding
    private var isim: String? = null
    private var gender_name: String? = null
    private lateinit var userViewModel: UserNameViewModel
    private lateinit var userModel: UserName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.userNameKaydet.setOnClickListener {
            userViewModel = ViewModelProvider(this).get(UserNameViewModel::class.java)
            if (userGenderCheck() && userNameCheck()) {
                saveToDatabase()
            }
        }
        binding.userNameKaydetme.setOnClickListener {
            goFragment(MainFragment())
        }
        super.onStart()
    }

    private fun goFragment(fragment: Fragment) {
        val frManager = requireActivity().supportFragmentManager
        val transaction = frManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment).commit()
    }

    private fun userGenderCheck(): Boolean {
        return if (!binding.kullaniciErkek.isChecked && !binding.kullaniciKadin.isChecked && !binding.kullaniciBelirtmedi.isChecked) {
            Toast.makeText(context, "Lütfen bir seçenek seçiniz !!!", Toast.LENGTH_LONG).show()
            false
        } else {
            when {
                binding.kullaniciErkek.isChecked -> {
                    gender_name = " BEY"
                }
                binding.kullaniciKadin.isChecked -> {
                    gender_name = " HANIM"
                }
                binding.kullaniciBelirtmedi.isChecked -> {
                    gender_name = ""
                }
            }
            true
        }
    }

    private fun userNameCheck(): Boolean {
        return if (binding.userName.text.isNullOrEmpty() || binding.userName.text!!.isBlank()) {
            Toast.makeText(context, "Lütfen adınızı giriniz", Toast.LENGTH_LONG).show()
            false
        } else {
            isim = binding.userName.text.toString()
            userModel = UserName(isim!!, gender_name)
            userViewModel.deleteUser()
            true
        }
    }

    private fun saveToDatabase() {
        userViewModel.addUser(userModel)
        goFragment(MainFragment())
    }
}
