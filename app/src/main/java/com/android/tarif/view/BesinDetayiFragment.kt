package com.android.tarif.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tarif.R
import com.android.tarif.adapter.BesinRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_besin_detayi.*
import kotlinx.android.synthetic.main.fragment_besin_listesi.*
import java.lang.Exception

class BesinDetayiFragment : Fragment() {

    private var besinId = 0
    var yemekIsmiListesi = ArrayList<String>()
    var yemekIdListesi = ArrayList<Int>()
    private lateinit var listeAdapter: BesinRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_detayi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqlVeriAlma()
    }

    fun sqlVeriAlma() {

        arguments?.let {

            val secilenId = BesinTarifFragmentArgs.fromBundle(it).besinId

            context?.let {
                try {
                    val db = it.openOrCreateDatabase("yemekler", Context.MODE_PRIVATE, null)
                    val cursor = db.rawQuery(
                        "SELECT * FROM yemekler WHERE id=?",
                        arrayOf(secilenId.toString())
                    )
                    val yemekIsmiIndex = cursor.getColumnIndex("yemekismi")
                    val yemekAciklamaIndex = cursor.getColumnIndex("yemekaciklama")
                    val yemekGorsel = cursor.getColumnIndex("yemekgorsel")

                    while (cursor.moveToNext()) {
                        besinIsmi.setText(cursor.getString(yemekIsmiIndex))
                        tarif.setText(cursor.getString(yemekAciklamaIndex))
                        val byteArray = cursor.getBlob(yemekGorsel)
                        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        besinImage.setImageBitmap(bitmap)
                    }
                    cursor.close()
                } catch (e: Exception) {

                }
            }
        }
    }


}