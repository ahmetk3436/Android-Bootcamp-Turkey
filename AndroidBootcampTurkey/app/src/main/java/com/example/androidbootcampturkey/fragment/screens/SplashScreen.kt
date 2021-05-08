package com.example.androidbootcampturkey.fragment.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidbootcampturkey.MainActivity
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.FragmentSplashScreenBinding

class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Handler(Looper.getMainLooper()).post {
            if (onBoardingFinished()) {
                binding.ivNote.alpha = 0f
                binding.ivNote.animate().alpha(1F).setDuration(2000L).withEndAction {
                    val i = Intent(context, MainActivity::class.java)
                    startActivity(i)
                }
            } else {
                binding.ivNote.alpha = 0f
                binding.ivNote.animate().alpha(1F).setDuration(2000L).withEndAction {
                    findNavController().navigate(R.id.action_splashScreen_to_viewPager)
                }
            }
        }
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}