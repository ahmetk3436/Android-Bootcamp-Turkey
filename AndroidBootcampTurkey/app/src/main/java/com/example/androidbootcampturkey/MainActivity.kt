package com.example.androidbootcampturkey

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbootcampturkey.databinding.ActivityMainBinding
import com.example.androidbootcampturkey.fragment.MainFragment
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        setContentView(view)
        goMainFragment()
    }

    private fun goMainFragment() {
        val bundle = Bundle()
        bundle.putString("para_fragment", intent.getStringExtra("para"))
        bundle.putString("para_fragment2", intent.getStringExtra("para_toplam"))
        val fragment = MainFragment()
        fragment.arguments = bundle
        val frManager = supportFragmentManager
        val transaction = frManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment).commit()
    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Geri gidemezsiniz ", Toast.LENGTH_LONG).show()
    }
}