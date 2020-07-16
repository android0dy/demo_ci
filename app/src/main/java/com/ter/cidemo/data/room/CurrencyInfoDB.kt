package com.ter.cidemo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ter.cidemo.data.room.model.CurrencyInfo

/**
 * Constant related to Database Schema
 */
const val DB_VERSION = 1
const val DB_NAME = "CurrencyInfo.db"

/**
 * Room Database declaration for CurrencyInfo
 */
@Database(entities = [CurrencyInfo::class], version = DB_VERSION, exportSchema = false)
abstract class CurrencyInfoDB: RoomDatabase(){

    abstract fun demoDAO(): CurrencyInfoDAO

    companion object{

        @Volatile private var instance: CurrencyInfoDB? = null

        /**
         * get CurrencyInfo Database Instance.
         * create instance of the database synchronized if does not exist
         */
        fun getCurrencyInfoDB(context: Context) : CurrencyInfoDB =
            instance ?: synchronized(this) {
                initializeInstance(context).also { instance = it }
            }

        /**
         * create instance for CurrencyInfo Database
         */
        private fun initializeInstance(context: Context) =
            Room.databaseBuilder(context, CurrencyInfoDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}