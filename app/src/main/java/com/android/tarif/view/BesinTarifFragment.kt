package com.android.tarif.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.android.tarif.dao.DAO
import com.android.tarif.R
import com.android.tarif.dao.Veritabani
import com.android.tarif.model.Besin
import kotlinx.android.synthetic.main.fragment_besin_tarif.*
import java.io.ByteArrayOutputStream

class BesinTarifFragment : Fragment() {

    var resim: Uri? = null
    var bitmap: Bitmap? = null
    lateinit var vt: Veritabani
    lateinit var yemekArrayList: ArrayList<Besin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_tarif, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aciklama.movementMethod = ScrollingMovementMethod()
        vt = Veritabani(requireActivity().applicationContext)
        yemekArrayList = DAO().tumYemekler(vt)

        imageView.setOnClickListener {
            gorselSec()
        }

        kaydetButton.setOnClickListener {
            kaydet(it)
        }
    }

    fun kaydet(view: View) {
        val yemekIsmi = besinIsim.text.toString()
        val yemekAciklama = aciklama.text.toString()

        if (bitmap != null) {
            val smallBitmap = small(bitmap!!, 1000)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()

            try {

                context?.let {
                    val db = it.openOrCreateDatabase("yemekler", Context.MODE_PRIVATE, null)
                    db.execSQL("CREATE TABLE IF NOT EXISTS yemekler (id INTEGER PRIMARY KEY, yemekismi VARCHAR, yemekaciklama VARCHAR, yemekgorsel BLOB)")

                    val sqliteString =
                        "INSERT INTO yemekler (yemekismi, yemekaciklama, yemekgorsel) VALUES (?,?,?)"
                    val statement = db.compileStatement(sqliteString)
                    statement.bindString(1, yemekIsmi)
                    statement.bindString(2, yemekAciklama)
                    statement.bindBlob(3, byteArray)
                    statement.execute()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val action =
                BesinTarifFragmentDirections.actionBesinTarifFragmentToBesinListesiFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun gorselSec() {

        activity.let {

            if (ContextCompat.checkSelfPermission(
                    it!!.applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            } else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            resim = data.data
            try {
                context?.let {
                    if (resim != null) {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(it.contentResolver, resim!!)
                            bitmap = ImageDecoder.decodeBitmap(source)
                            imageView.setImageBitmap(bitmap)

                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(it.contentResolver, resim)
                            imageView.setImageBitmap(bitmap)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    public fun small(x: Bitmap, max: Int): Bitmap {

        var width = x.width
        var height = x.height
        val scale: Double = width.toDouble() / height.toDouble()

        if (scale > 1) {
            width = max
            var newHeight = width / scale
            height = newHeight.toInt()
        } else {
            height = max
            var newWidth = height * scale
            width = newWidth.toInt()

        }
        return Bitmap.createScaledBitmap(x, width / 2, height / 2, true)

    }

}
