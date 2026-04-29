package com.example.lista3era

import android.app.Dialog
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


// Clase que muestra el mapa
class mapa(private val direccion: String) : DialogFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_mapa)


        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)

        // Agrega el mapa al contenedor
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

        return dialog
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        try {
            val geocoder = Geocoder(requireContext())
            val resultados = geocoder.getFromLocationName(direccion, 1)

            if (resultados != null && resultados.isNotEmpty()) {
                val ubicacion = resultados[0]
                val latLng = LatLng(ubicacion.latitude, ubicacion.longitude)
                map.addMarker(MarkerOptions().position(latLng).title(direccion))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
