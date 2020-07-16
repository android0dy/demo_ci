package com.ter.cidemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ter.cidemo.R
import com.ter.cidemo.data.room.model.CurrencyInfo

/**
 * ViewHolder class for Currency list, representing one CurrencyInfo item
 */
class CurrencyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val tvInitial: TextView = itemView.findViewById(R.id.tv_initial)
    private val tvName: TextView = itemView.findViewById(R.id.tv_name)
    private val tvSymbol: TextView = itemView.findViewById(R.id.tv_symbol)

    /**
     * clear the layout for viewholder reuse
     */
    private fun clear(){
        tvInitial.text = ""
        tvName.text = ""
        tvSymbol.text = ""
        itemView.setOnClickListener(null)
    }

    /**
     * bind currencyInfo to the viewholder
     */
    fun bind(info: CurrencyInfo, clickListener: CurrencyListAdapter.CurrencyListItemListener){
        clear()
        tvInitial.text = info.name[0].toString()
        tvName.text = info.name
        tvSymbol.text = info.symbol
        itemView.setOnClickListener {
            clickListener.onItemClick(info.id)
        }
    }
}

/**
 * Adapter class for the Currency Recycler View
 */
class CurrencyListAdapter: RecyclerView.Adapter<CurrencyListViewHolder>() {

    /**
     * Listener for item click in the currency list
     */
    interface CurrencyListItemListener{
        fun onItemClick(id: String)
    }

    /**
     * emit string change when user clicks on item
     */
    val onClickEvent = MutableLiveData<String>()
    var currencyList : MutableList<CurrencyInfo> = ArrayList()

    //region implementation
    override fun getItemCount(): Int {
        return currencyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(currencyList[position], clickListener)
    }
    //end region

    private val clickListener = object : CurrencyListItemListener{
        override fun onItemClick(id: String) {
            onClickEvent.value = id
        }
    }
}