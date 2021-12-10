package com.android.tarif.dao

import com.android.tarif.dao.Veritabani
import com.android.tarif.model.Besin
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class DAO {
    fun tumYemekler(vt: Veritabani): ArrayList<Besin> {
        val arrayList: ArrayList<Besin> = ArrayList<Besin>()
        val db = vt.writableDatabase
        val c = db.rawQuery("SELECT * FROM yemekler", null)
        while (c.moveToNext()) {
            val k = Besin(
                c.getString(c.getColumnIndexOrThrow("yemekismi")),
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("yemekaciklama")),
                c.getBlob(c.getColumnIndexOrThrow("yemekgorsel"))
            )
            arrayList.add(k)
        }
        return arrayList
    }

    fun delete(vt: Veritabani, id: Int) {
        val db = vt.writableDatabase
        db.delete("yemekler", "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun ekle(vt: Veritabani, yemekismi: String?, yemekaciklama: String?, yemekgorsel: ByteArray?) {
        val db = vt.writableDatabase
        val outputStream = ByteArrayOutputStream()
        val byteArray = outputStream.toByteArray()

        val sqliteString =
            "INSERT INTO yemekler (yemekismi, yemekaciklama, yemekgorsel) VALUES (?,?,?)"
        val statement = db.compileStatement(sqliteString)
        statement.bindString(1, yemekismi)
        statement.bindString(2, yemekaciklama)
        statement.bindBlob(3, byteArray)
        statement.execute()
    }

}