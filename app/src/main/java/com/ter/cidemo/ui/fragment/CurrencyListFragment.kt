package com.ter.cidemo.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ter.cidemo.R
import com.ter.cidemo.data.room.CurrencyInfoDB
import com.ter.cidemo.data.room.model.CurrencyInfo
import com.ter.cidemo.databinding.FragmentCurrencyListBinding
import com.ter.cidemo.ui.adapter.CurrencyListAdapter
import com.ter.cidemo.ui.scene.demo.CurrencyListViewModel

/**
 * The Fragment that contains Currency List
 */

class CurrencyListFragment: Fragment(){

    companion object{
        @JvmStatic
        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, value: Boolean) {
            view.visibility = if (value) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("currencyList")
        fun setCurrencyList(recyclerView: RecyclerView, dataList: ObservableField<List<CurrencyInfo>>){
            if (recyclerView.adapter != null){
                val adapter = recyclerView.adapter as CurrencyListAdapter
                adapter.currencyList = ArrayList()
                adapter.currencyList.addAll(dataList.get() ?: ArrayList())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private lateinit var mViewModel: CurrencyListViewModel
    private lateinit var mDataBinding: FragmentCurrencyListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(requireActivity())[CurrencyListViewModel::class.java]
        mViewModel.setDBInstance(CurrencyInfoDB.getCurrencyInfoDB(requireContext()))

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_currency_list, container, false)
        mDataBinding.lifecycleOwner = this
        mDataBinding.viewModel = mViewModel

        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCurrencyRecyclerView(view.context)
    }

    private fun setUpCurrencyRecyclerView(context: Context){
        mDataBinding.rvCurrency.layoutManager = LinearLayoutManager(context)

        val adapter = CurrencyListAdapter()
        adapter.onClickEvent.observe(viewLifecycleOwner, Observer { id ->
            Toast.makeText(context, "User has clicked on $id", Toast.LENGTH_LONG).show()
        })
        mDataBinding.rvCurrency.adapter = adapter
    }

    override fun onDestroy() {
        mDataBinding.unbind()
        super.onDestroy()
    }

    /**
     * Load data on user click
     */
    fun loadData(){
        mViewModel.getData()
    }

    fun sortData(){
        mViewModel.sortData()
    }
}