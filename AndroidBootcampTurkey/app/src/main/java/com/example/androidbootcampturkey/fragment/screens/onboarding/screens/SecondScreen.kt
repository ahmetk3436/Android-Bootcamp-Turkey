package com.example.androidbootcampturkey.fragment.screens.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.FragmentSecondScreenBinding

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager2)
        binding.next2.setOnClickListener {
            viewPager.currentItem = 2
        }
        setupIndicators()
        setCurrentIndicator()
        return view
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(viewPager.adapter!!.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(40, 40)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i].apply {
                (
                        this?.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.indicators_inactive
                            )
                        )
                        )
                this?.layoutParams = layoutParams
            }
            binding.indicatorContainers2.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator() {
        val childCount = binding.indicatorContainers2.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainers2[i] as ImageView
            if (i == 1) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicators_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicators_inactive
                    )
                )
            }
        }
    }
}