package com.ter.cidemo.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ter.cidemo.data.room.model.CurrencyInfo
import io.reactivex.Single

/**
 * Declaration of CurrencyInfo database operation methods
 */
@Dao
interface CurrencyInfoDAO {

    /**
     * single CurrencyInfo insert/update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyInfo(currencyInfoModel: CurrencyInfo)

    /**
     * list of CurrencyInfo insert/update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyInfoList(list: List<CurrencyInfo>)

    /**
     * load CurrencyInfo by id
     */
    @Query("SELECT * FROM CurrencyInfo WHERE id =:id")
    fun loadCurrencyInfoById(id: String?) : Single<CurrencyInfo>

    /**
     * load all currency
     */
    @Query("SELECT * FROM CurrencyInfo")
    fun loadAll(): Single<List<CurrencyInfo>>
}