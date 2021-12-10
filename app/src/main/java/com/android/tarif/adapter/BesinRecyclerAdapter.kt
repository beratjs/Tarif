package com.android.tarif.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.tarif.*
import com.android.tarif.dao.DAO
import com.android.tarif.dao.Veritabani
import com.android.tarif.model.Besin
import com.android.tarif.view.BesinListesiFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.recycler.view.*
import java.util.*
import kotlin.collections.ArrayList

class BesinRecyclerAdapter(var besinListesi: ArrayList<Besin>, private var vt: Veritabani) :
    RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>(), Filterable {

    var besinFilterList = ArrayList<Besin>()
    var yemekArrayList = ArrayList<Besin>()
    var yeniArrayList = ArrayList<Besin>()

    init {
        besinFilterList = besinListesi
        yemekArrayList = besinListesi
        yeniArrayList = besinListesi
    }

    class BesinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler, parent, false)
        vt = Veritabani(parent.context)

        return BesinViewHolder(view)
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        val besin: Besin = besinListesi.get(position)

        holder.itemView.vert.setOnClickListener {

            val popup = PopupMenu(holder.itemView.context, holder.itemView.vert)
            popup.inflate(R.menu.popup)
            val action =
                BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinTarifFragment()

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.action_sil -> {
                        Snackbar.make(
                            holder.itemView.vert,
                            "Yemek silinsin mi?",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Evet", View.OnClickListener {
                                DAO().delete(vt, besin.id!!)
                                besinFilterList = DAO().tumYemekler(vt)
                                notifyDataSetChanged()
                            }).show()
                    }

                }
                true
            })
            popup.show()
        }

        holder.itemView.recycler_isim.text = besinFilterList.get(position).yemekismi
        holder.itemView.recycler_aciklama.text = besinFilterList.get(position).yemekaciklama
        holder.itemView.imageView_rv.gorselIndir(besinFilterList.get(position).yemekgorsel)

        holder.itemView.setOnClickListener {
            val action =
                BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinDetayiFragment(
                    besinFilterList.get(position).id!!
                )
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return besinFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    besinFilterList = besinListesi
                } else {
                    val resultList = ArrayList<Besin>()
                    for (row in besinListesi) {
                        if (row.yemekismi!!.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    besinFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = besinFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                besinFilterList = results?.values as ArrayList<Besin>
                notifyDataSetChanged()
            }
        }
    }

}