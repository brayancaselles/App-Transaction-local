package com.example.appcredibanco.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcredibanco.data.database.dao.TransactionDao
import com.example.appcredibanco.data.database.entities.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class TransactionDataBase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDao
}
