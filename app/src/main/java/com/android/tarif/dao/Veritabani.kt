package com.android.tarif.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Veritabani(context: Context) : SQLiteOpenHelper(context, "yemekler", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db?.execSQL("CREATE TABLE yemekler (yemekismi VARCHAR, id INTEGER PRIMARY KEY,yemekaciklama VARCHAR, yemekgorsel BLOB);")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS yemekler")
        onCreate(db)
    }
}