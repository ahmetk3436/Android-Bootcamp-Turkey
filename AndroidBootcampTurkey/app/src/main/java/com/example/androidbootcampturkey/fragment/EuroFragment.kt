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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbootcampturkey.databinding.FragmentEuroBinding
import com.example.androidbootcampturkey.model.Model
import com.example.androidbootcampturkey.recycleradapter.EuroRecyclerViewAdapter
import com.example.androidbootcampturkey.repository.Repository
import com.example.androidbootcampturkey.viewmodel.FaturaViewModel
import com.example.androidbootcampturkey.viewmodel.MainViewModel
import com.example.androidbootcampturkey.viewmodel.MainViewModelFactory
import com.example.androidbootcampturkey.viewmodel.MoneyViewModel


class EuroFragment : Fragment() {
    private lateinit var binding: FragmentEuroBinding
    private lateinit var viewModel: MainViewModel
    private var recyclerMoneyAdapter: EuroRecyclerViewAdapter? = null
    private lateinit var moneyDatabase: MoneyViewModel
    private var model: Model? = null
    private lateinit var faturaDatabase: FaturaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEuroBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        getFaturaData()
        binding.euroRecyclerview.layoutManager = LinearLayoutManager(context)
        recyclerMoneyAdapter = EuroRecyclerViewAdapter(arrayListOf(), arrayListOf())
        binding.euroRecyclerview.adapter = recyclerMoneyAdapter
        super.onViewCreated(view, savedInstanceState)
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
        if (isOnline(requireContext().applicationContext)) {

            val repository = Repository()
            val viewModelFactory = MainViewModelFactory(repository)
            viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
            viewModel.getPost()
            viewModel.myResponse.observe(viewLifecycleOwner, { response ->
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
                    moneyDatabase.readAllData.observe(viewLifecycleOwner, { money ->
                        if (!money.isNullOrEmpty()) {
                            recyclerMoneyAdapter!!.setMoneyData(money)
                        }
                    })
                }
            })

        } else {
            moneyDatabase = ViewModelProvider(this).get(MoneyViewModel::class.java)
            moneyDatabase.readAllData.observe(viewLifecycleOwner, { money ->
                if (!money.isNullOrEmpty()) {
                    model = Model(
                        money.first().USD,
                        money.first().TRY,
                        money.first().EUR,
                        money.first().GBP
                    )
                    recyclerMoneyAdapter!!.setMoneyData(money)
                }
            })
            Toast.makeText(context, "Lütfen internetinizi açınız", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFaturaData() {
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(viewLifecycleOwner, { fatura ->
            recyclerMoneyAdapter!!.setData(fatura)
        })
    }
}