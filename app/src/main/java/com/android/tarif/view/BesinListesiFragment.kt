package com.android.tarif.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tarif.dao.DAO
import com.android.tarif.R
import com.android.tarif.dao.Veritabani
import com.android.tarif.adapter.BesinRecyclerAdapter
import com.android.tarif.databinding.FragmentBesinListesiBinding
import com.android.tarif.model.Besin
import kotlinx.android.synthetic.main.fragment_besin_listesi.*
import kotlin.collections.ArrayList

class BesinListesiFragment : Fragment() {

    var arrayList = ArrayList<Besin>()
    lateinit var binding: FragmentBesinListesiBinding
    lateinit var listeAdapter: BesinRecyclerAdapter
    lateinit var vt: Veritabani

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_besin_listesi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            val action =
                BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinTarifFragment(
                    0,
                    "fabdangeldim"
                )
            Navigation.findNavController(it).navigate(action)
        }

        vt = Veritabani(requireContext().applicationContext)
        listeAdapter = BesinRecyclerAdapter(arrayList, vt)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listeAdapter
        listeAdapter.notifyDataSetChanged()
        recyclerView.setHasFixedSize(true)
        tumYemeklerAl()

        search_fragment.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                listeAdapter.filter.filter(p0)
                return true
            }

        })
        sqlVeriAlma()

    }

    fun tumYemeklerAl() {
        arrayList = DAO().tumYemekler(vt)
        listeAdapter = BesinRecyclerAdapter(arrayList, vt)
        recyclerView.adapter = listeAdapter
    }

    fun sqlVeriAlma() {

        try {
            activity?.let {

                val db = it.openOrCreateDatabase("yemekler", Context.MODE_PRIVATE, null)
                val cursor = db.rawQuery("SELECT * FROM yemekler", null)
                val yemekIsmiIndex = cursor.getColumnIndex("yemekismi")
                val yemekIdIndex = cursor.getColumnIndex("id")
                val yemekAciklama = cursor.getColumnIndex("yemekaciklama")
                val yemekGorsel = cursor.getColumnIndex("yemekgorsel")
                arrayList.clear()

                while (cursor.moveToNext()) {
                    var name: String?
                    var id: Int?
                    var aciklama: String?
                    var gorsel: ByteArray?

                    name = cursor.getString(yemekIsmiIndex)
                    id = cursor.getInt(yemekIdIndex)
                    aciklama = cursor.getString(yemekAciklama)
                    gorsel = cursor.getBlob(yemekGorsel)
                    val deneme = Besin(name, id, aciklama, gorsel)
                    arrayList.add(deneme)

                }
                listeAdapter.notifyDataSetChanged()
                cursor.close()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}