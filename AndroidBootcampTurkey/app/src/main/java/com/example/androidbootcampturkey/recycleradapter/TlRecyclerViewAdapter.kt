package com.example.androidbootcampturkey.recycleradapter

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidbootcampturkey.DetailActivity
import com.example.androidbootcampturkey.R
import com.example.androidbootcampturkey.databinding.StandartRecyclerBinding
import com.example.androidbootcampturkey.model.FaturaData
import com.example.androidbootcampturkey.model.Model

class TlRecyclerViewAdapter(
    private var faturaList: List<FaturaData>, private var moneyList: List<Model>
) :
    RecyclerView.Adapter<TlRecyclerViewAdapter.RecyclerViewHolder>() {


    private var moneyler: Float = 0.0f

    class RecyclerViewHolder(var view: StandartRecyclerBinding) : RecyclerView.ViewHolder(view.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.standart_recycler, parent, false)
        val view = DataBindingUtil.inflate<StandartRecyclerBinding>(
            inflater,
            R.layout.standart_recycler,
            parent,
            false
        )
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.view.faturaData = faturaList[position]

        if (!moneyList.isNullOrEmpty()) {
            val currentItem = holder.view.faturaData!!
            val para = currentItem.para_miktari!!.toFloat()
            when (currentItem.para_birimi) {
                "tl" -> {
                    currentItem.para_miktari = currentItem.para_miktari
                }
                "dolar" -> {
                    val money = moneyList.first().USD.toFloat()

                    val sonuc = para * (1 / money)
                    currentItem.para_miktari = sonuc.toString()
                }
                "euro" -> {
                    val money = moneyList.first().EUR.toFloat()
                    val sonuc = para * (1 / money)
                    currentItem.para_miktari = sonuc.toString()
                }
                "sterlin" -> {
                    val money = moneyList.first().GBP.toFloat()
                    val sonuc = para * (1 / money)
                    currentItem.para_miktari = sonuc.toString()
                }
            }
            moneyler += currentItem.para_miktari!!.toFloat()
            when (currentItem.fatura_tipi) {
                "Fatura" -> {
                    Glide.with(holder.itemView).load(R.drawable.fatura).fitCenter()
                        .into(holder.view.faturaResmi)
                }
                "Kira" -> {
                    Glide.with(holder.itemView).load(R.drawable.ev_kira).fitCenter()
                        .into(holder.view.faturaResmi)
                }
                "Diger" -> {
                    Glide.with(holder.itemView).load(R.drawable.diger).fitCenter()
                        .into(holder.view.faturaResmi)
                }
            }
            Glide.with(holder.itemView).load(R.drawable.tl).fitCenter()
                .into(holder.view.paraBirimiResim)
            holder.view.button2.setOnClickListener { v ->
                val activity = v!!.context as AppCompatActivity
                val positionId = holder.layoutPosition
                if (positionId >= 0) {
                    try {
                        val intent = Intent(activity.applicationContext, DetailActivity::class.java)
                        intent.putExtra(
                            "fatura_tutari",
                            faturaList[positionId].para_miktari.toString()
                        )
                        intent.putExtra(
                            "fatura_aciklamasi",
                            faturaList[positionId].fatura_aciklamasi.toString()
                        )
                        intent.putExtra("para_birimi", "tl")
                        intent.putExtra(
                            "fatura_tipi",
                            faturaList[positionId].fatura_tipi.toString()
                        )
                        intent.putExtra("fatura_id", currentItem.id)
                        intent.putExtra("fatura_id2", positionId)
                        intent.putExtra("para_toplam_tl", moneyler.toString())
                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            activity,
                            activity.findViewById(R.id.start_view_main),
                            "shared_element_container" // The transition name to be matched in Activity B.
                        )
                        activity.startActivity(intent, options.toBundle())
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return faturaList.size
    }

    fun setMoneyData(money: List<Model>) {
        this.moneyList = money
        notifyDataSetChanged()
    }

    fun setData(fatura: List<FaturaData>) {
        this.faturaList = fatura
        notifyDataSetChanged()
    }
}


