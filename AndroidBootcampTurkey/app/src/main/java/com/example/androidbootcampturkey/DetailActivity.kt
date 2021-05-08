package com.example.androidbootcampturkey


import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.androidbootcampturkey.databinding.ActivityDetailBinding
import com.example.androidbootcampturkey.model.FaturaData
import com.example.androidbootcampturkey.recycleradapter.TlRecyclerViewAdapter
import com.example.androidbootcampturkey.viewmodel.FaturaViewModel
import com.example.androidbootcampturkey.viewmodel.MoneyViewModel
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    lateinit var fatura: FaturaData
    private var recyclerMoneyAdapter: TlRecyclerViewAdapter? = null
    private lateinit var faturaDatabase: FaturaViewModel
    private lateinit var moneyDatabase: MoneyViewModel
    private var para_birimi: String? = null
    private var sonuc: Float = 0.0f
    var tl: Float = 0.0f
    var euro: Float = 0.0f
    var usd: Float = 0.0f
    var sterlin: Float = 0.0f
    private var paraToplam: Float = 0.0f
    var fatura_id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        binding.endViewMain.transitionName = "shared_element_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.endViewMain)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(binding.endViewMain)
            duration = 250L
        }
        setContentView(view)
        getDetail()
        getMoneyData()
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        fatura_id = intent.getIntExtra("fatura_id", 1000000)
        recyclerMoneyAdapter = TlRecyclerViewAdapter(arrayListOf(), arrayListOf())
    }

    fun getMoneyData() {
        moneyDatabase = ViewModelProvider(this).get(MoneyViewModel::class.java)
        moneyDatabase.readAllData.observe(this, { paraciklar ->
            if (!paraciklar.isNullOrEmpty()) {
                tl = paraciklar.first().TRY.toFloat()
                usd = paraciklar.first().USD.toFloat()
                euro = paraciklar.first().EUR.toFloat()
                sterlin = paraciklar.first().GBP.toFloat()
            }
        })
    }

    override fun onBackPressed() {
        GlobalScope.launch {
            delay(400L)
            val paraToplam = intent.getStringExtra("para_toplam_tl")
            val intent2 = Intent(applicationContext, MainActivity::class.java)
            intent2.putExtra("para", para_birimi)
            intent2.putExtra("para_toplam", paraToplam)
            startActivity(intent2)
        }

    }

    private fun getDetail() {
        binding.faturaAciklamasiRecyclerOnClick.text = intent.getStringExtra("fatura_aciklamasi")
        binding.faturaTutariRecyclerOnClick.text = intent.getStringExtra("fatura_tutari")

        when (intent.getStringExtra("fatura_tipi")) {
            "Fatura" -> {
                Glide.with(applicationContext).load(R.drawable.fatura).fitCenter()
                    .into(binding.faturaResmiRecyclerOnClick)
            }
            "Kira" -> {
                Glide.with(applicationContext).load(R.drawable.ev_kira).fitCenter()
                    .into(binding.faturaResmiRecyclerOnClick)
            }
            "Diger" -> {
                Glide.with(applicationContext).load(R.drawable.diger).fitCenter()
                    .into(binding.faturaResmiRecyclerOnClick)
            }
        }

        when (intent.getStringExtra("para_birimi")) {
            "tl" -> {
                para_birimi = "tl"
                Glide.with(applicationContext).load(R.drawable.tl).fitCenter()
                    .into(binding.paraBirimiResimRecyclerOnClick)
            }
            "dolar" -> {
                para_birimi = "dolar"
                Glide.with(applicationContext).load(R.drawable.dolar).fitCenter()
                    .into(binding.paraBirimiResimRecyclerOnClick)
            }
            "euro" -> {
                para_birimi = "euro"
                Glide.with(applicationContext).load(R.drawable.euro).fitCenter()
                    .into(binding.paraBirimiResimRecyclerOnClick)
            }
            "sterlin" -> {
                para_birimi = "sterlin"
                Glide.with(applicationContext).load(R.drawable.sterlin).fitCenter()
                    .into(binding.paraBirimiResimRecyclerOnClick)
            }
        }
        binding.deleteDataButton.setOnClickListener {
            faturaDatabase.deleteFatura(fatura_id)
            when (para_birimi) {
                "tl" -> {
                    tlData()
                }
                "dolar" -> {
                    usdData()
                }
                "euro" -> {
                    euroData()
                }
                "sterlin" -> {
                    sterlinData()
                }
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(500L)
                recyclerMoneyAdapter!!.notifyItemRemoved(
                    intent.getIntExtra(
                        "fatura_id2",
                        10000000
                    )
                )
                paraToplam = sonuc
                println(paraToplam)
                val intent2 = Intent(applicationContext, MainActivity::class.java)
                intent2.putExtra("para", para_birimi)
                intent2.putExtra("para_toplam", paraToplam.toString())
                startActivity(intent2)
            }
        }
    }

    private fun tlData(): Float {
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty())
                fatura.forEach { ack ->
                    GlobalScope.launch(Dispatchers.Main) {
                        when (ack.para_birimi) {
                            "tl" -> {
                                para = ack.para_miktari!!.toFloat()
                            }
                            "dolar" -> {
                                val sonuc_dolar =
                                    (ack.para_miktari!!.toFloat() * (1.0 / usd).toFloat())
                                para2 = sonuc_dolar
                            }
                            "euro" -> {
                                val sonuc_euro =
                                    (ack.para_miktari!!.toFloat() * (1.0 / euro).toFloat())
                                para3 = sonuc_euro
                            }
                            "sterlin" -> {
                                val sonuc_sterlin =
                                    (ack.para_miktari!!.toFloat() * (1.0 / sterlin).toFloat())
                                para4 = sonuc_sterlin
                            }
                        }
                    }
                    sonuc = para + para2 + para3 + para4
                }
        })
        return sonuc
    }

    private fun usdData(): Float {
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty())
                fatura.forEach { ack ->
                    GlobalScope.launch(Dispatchers.Main) {
                        when (ack.para_birimi) {
                            "tl" -> {
                                val sonuc_tl = (ack.para_miktari!!.toFloat() * usd)
                                para = sonuc_tl
                            }
                            "dolar" -> {
                                val sonuc_dolar = (ack.para_miktari!!.toFloat())
                                para2 = sonuc_dolar
                            }
                            "euro" -> {
                                val sonuc_euro =
                                    (ack.para_miktari!!.toFloat() * (1.0 / euro).toFloat() * usd)
                                para3 = sonuc_euro
                            }
                            "sterlin" -> {
                                val sonuc_sterlin =
                                    (ack.para_miktari!!.toFloat() * (1.0 / sterlin).toFloat() * usd)
                                para4 = sonuc_sterlin
                            }
                        }
                    }

                    sonuc = para + para2 + para3 + para4
                }
        })
        return sonuc
    }

    fun euroData(): Float {
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty())
                fatura.forEach { ack ->
                    GlobalScope.launch(Dispatchers.Main) {
                        when (ack.para_birimi) {
                            "tl" -> {
                                val sonuc_tl = (ack.para_miktari!!.toFloat() * euro)
                                para = sonuc_tl
                            }
                            "dolar" -> {
                                val sonuc_dolar =
                                    (ack.para_miktari!!.toFloat() * (euro) * (1.0 / usd).toFloat())
                                para2 = sonuc_dolar
                            }
                            "euro" -> {
                                val sonuc_euro = (ack.para_miktari!!.toFloat())
                                para3 = sonuc_euro
                            }
                            "sterlin" -> {
                                val sonuc_sterlin =
                                    (ack.para_miktari!!.toFloat() * euro * (1 / sterlin))
                                para4 = sonuc_sterlin
                            }
                        }
                    }
                    sonuc = para + para2 + para3 + para4
                }
        })
        return sonuc
    }

    private fun sterlinData(): Float {
        var para = 0.0F
        var para2 = 0.0F
        var para3 = 0.0F
        var para4 = 0.0F
        faturaDatabase = ViewModelProvider(this).get(FaturaViewModel::class.java)
        faturaDatabase.readAllFatura.observe(this, { fatura ->
            if (!fatura.isNullOrEmpty())
                fatura.forEach { ack ->
                    GlobalScope.launch {
                        when (ack.para_birimi) {
                            "tl" -> {
                                val sonuc_tl = (ack.para_miktari!!.toFloat() * sterlin)
                                para = sonuc_tl
                            }
                            "dolar" -> {
                                val sonuc_dolar =
                                    (ack.para_miktari!!.toFloat() * sterlin * (1 / usd))
                                para2 = sonuc_dolar
                            }
                            "euro" -> {
                                val sonuc_euro =
                                    (ack.para_miktari!!.toFloat() * sterlin * (1 / euro))
                                para3 = sonuc_euro
                            }
                            "sterlin" -> {
                                val sonuc_sterlin = (ack.para_miktari!!.toFloat())
                                para4 = sonuc_sterlin
                            }
                        }
                    }
                    sonuc = para + para2 + para3 + para4
                }
        })
        return sonuc
    }
}