package com.example.pepitalk.Logica

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageButton
import com.example.pepitalk.Datos.Data
import com.example.pepitalk.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager

class Ruta : AppCompatActivity() {

    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1001
    private lateinit var map: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var startPoint: GeoPoint? = null
    private var endPoint: GeoPoint? = null
    private var roadOverlay: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(R.layout.activity_ruta)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        pedirPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Se necesita este permiso para acceder a tu ubicación.", PERMISSION_REQUEST_ACCESS_FINE_LOCATION)


        val menuInicio = findViewById<ImageButton>(R.id.butInicio)
        val perfil = findViewById<ImageButton>(R.id.butPerfil)
        val reunion = findViewById<Button>(R.id.btnDevolverReunion)
        // Solicita el permiso de acceso a la ubicación

        irPerfil(perfil, this)
        menuPrincipal(menuInicio, this)
        devolverReunion(reunion, this)
    }

    private fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val startLat = it.latitude
                    val startLng = it.longitude
                    startPoint = GeoPoint(startLat, startLng)

                    // Aquí ajusta la lógica para configurar la ruta y los marcadores
                    setupMap()
                } ?: run {
                    // Si no se encuentra la ubicación, puedes manejar el error aquí
                    Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupMap() {
        val endLat = 37.768009  //TIENEN QUE LLEGAR DE OTRA PANTALLA
        val endLng = -122.387787
        endPoint = GeoPoint(endLat, endLng)

        val startMarker = Marker(map)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(startMarker)

        val endMarker = Marker(map)
        endMarker.position = endPoint
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(endMarker)

        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        CoroutineScope(Dispatchers.Main).launch {
            drawRoute(startPoint!!, endPoint!!)

            val midLat = (startPoint!!.latitude + endPoint!!.latitude) / 2
            val midLng = (startPoint!!.longitude + endPoint!!.longitude) / 2
            val midPoint = GeoPoint(midLat, midLng)


            var boundingBox = BoundingBox.fromGeoPoints(listOf(startPoint, endPoint))

            val margin = 0.01
            boundingBox = BoundingBox(
                boundingBox.latNorth + margin,
                boundingBox.lonEast + margin,
                boundingBox.latSouth - margin,
                boundingBox.lonWest - margin
            )

            map.controller.setCenter(midPoint)
            map.zoomToBoundingBox(boundingBox, true)
        }
    }


    fun devolverReunion(reunion: Button, context: Context){
        val irAReunion = Intent(this, VerReunion::class.java)
        reunion.setOnClickListener {
            startActivity(irAReunion)
        }
    }

    private fun pedirPermiso(permiso: String, justificacion: String, idCode: Int) {

        if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                // Mostrar una justificación si es necesario

            }
            // Solicita el permiso
            ActivityCompat.requestPermissions(this, arrayOf(permiso), idCode)
            map.visibility = MapView.GONE
        } else {
            // Si ya tiene el permiso, muestra un mensaje y accede a la ubicación
            obtenerUbicacionActual()
            map.visibility = MapView.VISIBLE
        }
    }

    // Gestionar la respuesta del usuario
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el permiso fue concedido, accede a la ubicación
                    //initLocationAccess()
                    obtenerUbicacionActual()
                    map.visibility = MapView.VISIBLE
                } else {
                    // Si el permiso fue denegado, muestra un mensaje
                    map.visibility = MapView.GONE
                    Toast.makeText(this, "Funcionalidades limitadas por falta de acceso a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Ignorar otras solicitudes de permisos.
            }
        }
    }
    fun menuPrincipal(menuInicio: ImageButton, context: Context){
        menuInicio.setOnClickListener {
            if(Data.personaLog.tipo == "Cliente"){
                startActivity(Intent(this, MenuCliente::class.java))
            }
            else{
                startActivity(Intent(this, MenuTraductor::class.java))
            }
            Toast.makeText(this,"Yendo al menú", Toast. LENGTH_LONG).show()
        }
    }
    fun irPerfil(perfil : ImageButton, context : Context){
        val irAPerfil = Intent(this, Perfil::class.java)
        perfil.setOnClickListener {
            startActivity(irAPerfil)
            Toast.makeText(this,"¡Tu perfil!", Toast. LENGTH_LONG).show()
        }
    }

    private suspend fun drawRoute(start: GeoPoint, finish: GeoPoint) {
        val road = withContext(Dispatchers.IO) {
            val roadManager = OSRMRoadManager(this@Ruta, "YOUR_USER_AGENT")
            val waypoints = ArrayList<GeoPoint>()
            waypoints.add(start)
            waypoints.add(finish)
            roadManager.getRoad(waypoints)
        }

        if (road.mStatus != Road.STATUS_OK) {
            return
        }

        if (::map.isInitialized) {
            roadOverlay?.let { map.overlays.remove(it) }
            roadOverlay = RoadManager.buildRoadOverlay(road).apply {
                outlinePaint.color = resources.getColor(R.color.blue, theme)
                outlinePaint.strokeWidth = 10f
            }
            map.overlays.add(roadOverlay)
            map.invalidate()
        }
    }

}