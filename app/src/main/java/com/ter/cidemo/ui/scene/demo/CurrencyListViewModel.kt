package com.ter.cidemo.ui.scene.demo

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.ter.cidemo.BuildConfig
import com.ter.cidemo.data.api.ApiProvider
import com.ter.cidemo.data.room.CurrencyInfoDB
import com.ter.cidemo.data.room.model.CurrencyInfo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * View Model class for Currency List Recycler view
 */
class CurrencyListViewModel : ViewModel() {

    val currencyListObservable = ObservableField<List<CurrencyInfo>>()
    val isLoading = androidx.databinding.ObservableBoolean(false)

    private val apiService = ApiProvider.createApiService()
    private val mCompositeDisposable = CompositeDisposable()

    private var mCurrencyInfoDB: CurrencyInfoDB? = null

    /**
     * set instance for CurrencyInfoDatabase
     */
    fun setDBInstance(instance: CurrencyInfoDB) {
        this.mCurrencyInfoDB = instance
    }

    /**
     * Call this function to fetch data from DB & API
     */
    fun getData() {
        isLoading.set(true)
        val loadFromDB = loadCurrencyInfoFromDB()
        val loadFromApi = loadCurrencyInfoFromAPI()

        Single.concat(loadFromDB?.onErrorResumeNext{
            it.printStackTrace()
            return@onErrorResumeNext Single.fromObservable(Observable.empty<List<CurrencyInfo>>())
        }, loadFromApi?.onErrorResumeNext {
            it.printStackTrace()
            return@onErrorResumeNext Single.fromObservable(Observable.empty<List<CurrencyInfo>>())
        })
            .observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                isLoading.set(false)
                currencyListObservable.set(it)
            },{
                isLoading.set(false)
                it.printStackTrace()
            })
            ?.addTo(mCompositeDisposable)
    }

    /**
     * return a Single of List of CurrencyInfo from DB
     */
    private fun loadCurrencyInfoFromDB(): Single<List<CurrencyInfo>>? =
        mCurrencyInfoDB?.demoDAO()?.loadAll()
            ?.subscribeOn(Schedulers.io())

    /**
     * return a Single of List of CurrencyInfo from API
     */
    private fun loadCurrencyInfoFromAPI(): Single<List<CurrencyInfo>>? =
        apiService.obtainData(BuildConfig.DATA_URL)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {list ->
                Observable.create<Boolean> {
                    mCurrencyInfoDB?.demoDAO()?.insertCurrencyInfoList(list)
                    it.onComplete()
                }.subscribeOn(Schedulers.computation()).subscribe()
                return@map list
            }.subscribeOn(Schedulers.computation())

    /**
     * sort list by comparing to their name on a worker thread and update on main thread
     */
    fun sortData(){
        isLoading.set(true)

        Observable.fromIterable(currencyListObservable.get())
            .toSortedList{ c1, c2 ->
                c1.name.compareTo(c2.name)
            }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                isLoading.set(false)
            }
            .subscribe ({ list ->
                isLoading.set(false)
                currencyListObservable.set(list)
            }, Throwable::printStackTrace)
            .addTo(mCompositeDisposable)
    }

    /**
     * onClear of ViewModel
     */
    override fun onCleared() {
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
        super.onCleared()
    }
}