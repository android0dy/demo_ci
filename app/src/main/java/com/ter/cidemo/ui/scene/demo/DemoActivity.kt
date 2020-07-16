package com.ter.cidemo.ui.scene.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ter.cidemo.R
import com.ter.cidemo.databinding.ActivityMainBinding
import com.ter.cidemo.ui.fragment.CurrencyListFragment
import java.lang.ref.WeakReference

/**
 * The Main Activity of the Demo app.
 *
 */

class DemoActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mListFragmentWeakReference: WeakReference<CurrencyListFragment>

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this

        setUpView()
    }

    override fun onDestroy() {
        mListFragmentWeakReference.clear()
        mBinding.unbind()
        super.onDestroy()
    }
    //endregion

    //region view set up
    private fun setUpView(){
        addListFragment()

        mBinding.btnLoad.setOnClickListener {
            mListFragmentWeakReference.get()?.loadData()
        }
        mBinding.btnSort.setOnClickListener {
            mListFragmentWeakReference.get()?.sortData()
        }
    }

    private fun addListFragment(){
        mListFragmentWeakReference = WeakReference(CurrencyListFragment())

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main, mListFragmentWeakReference.get()!!, CurrencyListFragment::class.java.simpleName)
            .commit()
    }
    //endregion
}
