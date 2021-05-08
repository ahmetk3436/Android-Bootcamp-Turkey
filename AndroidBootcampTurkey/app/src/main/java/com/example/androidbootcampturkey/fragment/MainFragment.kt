package com.example.androidbootcampturkey.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.FragmentMainBinding
import com.example.androidbootcampturkey.model.Model
import com.example.androidbootcampturkey.model.UserName
import com.example.androidbootcampturkey.repository.Repository
import com.example.androidbootcampturkey.viewmodel.*
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.delay

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userViewModel: UserNameViewModel
    private lateinit var moneyDatabase: MoneyViewModel
    private lateinit var faturaDatabase: FaturaViewModel
    lateinit var userName: UserName
    private var model: Model? = null
    var tl: Float = 0.0F
    var usd: Float = 0.0F
    var euro: Float = 0.0F
    var sterlin: Float = 0.0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserNameViewModel::class.java)
        lifecycle.coroutineScope.launchWhenStarted {
            delay(200L)
            getData()
        }

        binding.dolarButonu.setOnClickListener {
            tlData()
        }
        binding.floatingActionButton.setOnClickListener {
            val frManager = requireActivity().supportFragmentManager
            val transaction = frManager.beginTransaction()
            transaction.replace(R.id.frameLayout, FaturaEklemeFragment()).commit()
        }
        binding.dolarButonu2.setOnClickListener {
            usdData()
        }
        binding.dolarButonu3.setOnClickListener {
            euroData()
        }
        binding.dolarButonu4.setOnClickListener {
            sterlinData()
        }
        binding.button2.setOnClickListener {
            val fragment = NameFragment()
            fragment.sharedElementEnterTransition = MaterialContainerTransform().setDuration(2000L)
            val frManager = requireActivity().supportFragmentManager
            frManager.beginTransaction()
                .addSharedElement(binding.startView, "shared_element_container")
                .replace(R.id.frameLayout, fragment, fragment.tag)
                .addToBackStack(fragment.tag)
                .commit()
        }
        getUserNameData()
        lifecycle.coroutineScope.launchWhenStarted {
            delay(300L)
            getFragment(TryFragment())
        }

        lifecycleScope.launchWhenStarted {
            delay(700L)
            getTlFatura()
        }
        lifecycleScope.launchWhenStarted {
            delay(1200L)
            getMoneyFragmentWithBundle()
        }
        super.onStart()
    }

    private fun getMoneyFragmentWithBundle() {
        arguments?.let {
            if (!requireArguments().getString("para_fragment")
                    .isNullOrEmpty() && !requireArguments().getString("para_fragment2")
                    .isNullOrEmpty()
            ) {
                println(requireArguments().getString("para_fragment"))
                when (requireArguments().getString("para_fragment")) {
                    "tl" -> {
                        getFragment(TryFragment())
                        binding.kullaniciMainFaturaTutar.text =
                            requireArguments().getString("para_fragment2")
                        Glide.with(requireContext()).load(R.drawable.tl).fitCenter()
                            .into(binding.kullaniciMainParaBirimiResim)
                    }
                    "dolar" -> {
                        getFragment(UsdFragment())
                        binding.kullaniciMainFaturaTutar.text =
                            requireArguments().getString("para_fragment2")
                        Glide.with(requireContext()).load(R.drawable.dolar).fitCenter()
                            .into(binding.kullaniciMainParaBirimiResim)
                    }
                    "euro" -> {
                        getFragment(EuroFragment())
                        binding.kullaniciMainFaturaTutar.text =
                            requireArguments().getString("para_fragment2")
                        Glide.with(requireContext()).load(R.drawable.euro).fitCenter()
                            .into(binding.kullaniciMainParaBirimiResim)
                    }
                    "sterlin" -> {
                        getFragment(SterlinFragment())
                        binding.kullaniciMainFaturaTutar.text =
                            requireArguments().getString("para_fragment2")
                        Glide.with(requireContext()).load(R.drawable.sterlin).fitCenter()
                            .into(binding.kullaniciMainParaBirimiResim)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getData() {
        if (isOnline(requireContext())) {
            val repository = Repository()
            val viewModelFactory = MainViewModelFactory(repository)
            viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
            viewModel.getPost()
            viewModel.myResponse.observe(this, { response ->
                if (response.isSuccessful) {
                    moneyDatabase = ViewModelProvider(this).get(MoneyViewModel::class.java)
                    model = Model(
                        response.body()?.data!!.USD,
                        response.body()?.data!!.TRY,
                        response.body()?.data!!.EUR,
                        response.body()?.data!!.GBP
                    )
                    moneyDatabase.deleteMoney2()
                    moneyDatabase.addMoney(model!!)
                    tl = response.body()?.data!!.TRY.toFloat()
                    usd = response.body()?.data!!.USD.toFloat()
                    euro = response.body()?.data!!.EUR.toFloat()
                    sterlin = response.body()?.data!!.GBP.toFloat()
                }
            })
        } else {
            moneyDatabase.readAllData.observe(this, { paraciklar ->
                if (!paraciklar.isNullOrEmpty()) {
                    tl = paraciklar.first().TRY.toFloat()
                    usd = paraciklar.first().USD.toFloat()
                    euro = paraciklar.first().EUR.toFloat()
                    sterlin = paraciklar.first().GBP.toFloat()
                }
            })
            Toast.makeText(requireContext(), "Lütfen internetinizi açınız", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout2, fragment).commit()
    }

    private fun getUserNameData() {
        userViewModel.readAllUser.observe(this, { user ->
            if (!user.isNullOrEmpty()) {
                binding.kullaniciMainAdi.text =
                    (user.first().user_name + user.first().gender_name.toString())
            }
        })
    }

    private fun getTlFatura() {
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty()) {
                fatura.forEach { faturas ->
                    when (faturas.para_birimi) {
                        "tl" -> {
                            para = faturas.para_miktari!!.toFloat()
                        }
                        "dolar" -> {
                            val sonuc_dolar =
                                (faturas.para_miktari!!.toFloat() * (1.0 / usd).toFloat())
                            para2 = sonuc_dolar
                        }
                        "euro" -> {
                            val sonuc_euro =
                                (faturas.para_miktari!!.toFloat() * (1.0 / euro).toFloat())
                            para3 = sonuc_euro
                        }
                        "sterlin" -> {
                            val sonuc_sterlin =
                                (faturas.para_miktari!!.toFloat() * (1.0 / sterlin).toFloat())
                            para4 = sonuc_sterlin
                        }
                    }
                    val sonuc = para + para2 + para3 + para4
                    binding.kullaniciMainFaturaTutar.text = sonuc.toString()
                    Glide.with(this).load(R.drawable.tl).fitCenter()
                        .into(binding.kullaniciMainParaBirimiResim)
                }
            } else {
                Toast.makeText(context, "Şuanda faturanız bulunmamaktadır", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun tlData() {
        getFragment(TryFragment())
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty()) {
                fatura.forEach { faturas ->
                    when (faturas.para_birimi) {
                        "tl" -> {
                            para = faturas.para_miktari!!.toFloat()
                        }
                        "dolar" -> {
                            val sonuc_dolar =
                                (faturas.para_miktari!!.toFloat() * (1.0 / usd).toFloat())
                            para2 = sonuc_dolar
                        }
                        "euro" -> {
                            val sonuc_euro =
                                (faturas.para_miktari!!.toFloat() * (1.0 / euro).toFloat())
                            para3 = sonuc_euro
                        }
                        "sterlin" -> {
                            val sonuc_sterlin =
                                (faturas.para_miktari!!.toFloat() * (1.0 / sterlin).toFloat())
                            para4 = sonuc_sterlin
                        }
                    }
                    val sonuc = (para + para2 + para3 + para4)
                    binding.kullaniciMainFaturaTutar.text = sonuc.toString()
                    Glide.with(this).load(R.drawable.tl).fitCenter()
                        .into(binding.kullaniciMainParaBirimiResim)
                }
            } else {
                Toast.makeText(context, "Şuanda faturanız bulunmamaktadır", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun usdData() {
        getFragment(UsdFragment())
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty()) {
                fatura.forEach { faturas ->
                    when (faturas.para_birimi) {
                        "tl" -> {
                            val sonuc_tl = (faturas.para_miktari!!.toFloat() * usd)
                            para = sonuc_tl
                        }
                        "dolar" -> {
                            val sonuc_dolar = (faturas.para_miktari!!.toFloat())
                            para2 = sonuc_dolar
                        }
                        "euro" -> {
                            val sonuc_euro =
                                (faturas.para_miktari!!.toFloat() * (1.0 / euro).toFloat() * usd)
                            para3 = sonuc_euro
                        }
                        "sterlin" -> {
                            val sonuc_sterlin =
                                (faturas.para_miktari!!.toFloat() * (1.0 / sterlin).toFloat() * usd)
                            para4 = sonuc_sterlin
                        }
                    }
                    val sonuc = para + para2 + para3 + para4
                    binding.kullaniciMainFaturaTutar.text = sonuc.toString()
                    Glide.with(this).load(R.drawable.dolar).fitCenter()
                        .into(binding.kullaniciMainParaBirimiResim)
                }
            } else {
                Toast.makeText(context, "Şuanda faturanız bulunmamaktadır", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun euroData() {
        getFragment(EuroFragment())
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty()) {
                fatura.forEach { faturas ->
                    when (faturas.para_birimi) {
                        "tl" -> {
                            val sonuc_tl = (faturas.para_miktari!!.toFloat() * euro)
                            para = sonuc_tl
                        }
                        "dolar" -> {
                            val sonuc_dolar =
                                (faturas.para_miktari!!.toFloat() * (euro) * (1.0 / usd).toFloat())
                            para2 = sonuc_dolar
                        }
                        "euro" -> {
                            val sonuc_euro = (faturas.para_miktari!!.toFloat())
                            para3 = sonuc_euro
                        }
                        "sterlin" -> {
                            val sonuc_sterlin =
                                (faturas.para_miktari!!.toFloat() * euro * (1 / sterlin))
                            para4 = sonuc_sterlin
                        }
                    }
                    val sonuc = para + para2 + para3 + para4
                    binding.kullaniciMainFaturaTutar.text = sonuc.toString()
                    Glide.with(this).load(R.drawable.euro).fitCenter()
                        .into(binding.kullaniciMainParaBirimiResim)
                }
            } else {
                Toast.makeText(context, "Şuanda faturanız bulunmamaktadır", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun sterlinData() {
        getFragment(SterlinFragment())
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty()) {
                fatura.forEach { ack ->
                    when (ack.para_birimi) {
                        "tl" -> {
                            val sonuc_tl = (ack.para_miktari!!.toFloat() * sterlin)
                            para = sonuc_tl
                        }
                        "dolar" -> {
                            val sonuc_dolar = (ack.para_miktari!!.toFloat() * sterlin * (1 / usd))
                            para2 = sonuc_dolar
                        }
                        "euro" -> {
                            val sonuc_euro = (ack.para_miktari!!.toFloat() * sterlin * (1 / euro))
                            para3 = sonuc_euro
                        }
                        "sterlin" -> {
                            val sonuc_sterlin = (ack.para_miktari!!.toFloat())
                            para4 = sonuc_sterlin
                        }
                    }
                    val sonuc = para + para2 + para3 + para4
                    binding.kullaniciMainFaturaTutar.text = sonuc.toString()
                    Glide.with(this).load(R.drawable.sterlin).fitCenter()
                        .into(binding.kullaniciMainParaBirimiResim)
                }
            } else {
                Toast.makeText(context, "Şuanda faturanız bulunmamaktadır", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}