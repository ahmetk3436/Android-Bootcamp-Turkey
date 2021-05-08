package com.example.androidbootcampturkey.fragment.screens.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidbootcampturkey.databinding.FragmentViewPagerBinding
import com.example.androidbootcampturkey.fragment.screens.onboarding.screens.FirstScreen
import com.example.androidbootcampturkey.fragment.screens.onboarding.screens.SecondScreen
import com.example.androidbootcampturkey.fragment.screens.onboarding.screens.ThirdScreen

class ViewPager : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        return view
    }
}