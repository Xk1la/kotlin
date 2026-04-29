package com.example.lista3era

import android.os.Bundle
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerOpciones: Spinner
    private lateinit var spinnerUnidad: Spinner
    private lateinit var spinnerConductores: Spinner
    private lateinit var KmSalida: EditText
    private lateinit var KmLlegada: EditText
    private lateinit var editObac: EditText
    private lateinit var editDireccion: EditText
    private lateinit var editBomberos: EditText
    private lateinit var editObservaciones: EditText
    private lateinit var btnEnviar: Button
    private lateinit var btnNuevaLista: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //son las referencias
        spinnerOpciones = findViewById(R.id.spinner2)
        spinnerUnidad = findViewById(R.id.spinner3)
        spinnerConductores = findViewById(R.id.spinner4)
        KmSalida = findViewById(R.id.editTextText3)
        KmLlegada = findViewById(R.id.editTextText4)
        editObac = findViewById(R.id.editTextText)
        editDireccion = findViewById(R.id.editTextText2)
        editBomberos = findViewById(R.id.editTextText6)
        editObservaciones = findViewById(R.id.editTextText5)
        btnEnviar = findViewById(R.id.button)
        btnNuevaLista = findViewById(R.id.button2)

        //valida el idioma y lo toma dependiendo si tu pc o celular esta en español
        val idioma = Locale.getDefault().language

        // valida las opcioens y tambien detecta si tu pc o celu esta en ingles se muestra en ingles y si esta en español se muestra en español
        val opciones = if (idioma == "en") arrayOf(
            "Select type of emergency",
            "1st Fire Alarm", "2nd Fire Alarm", "3rd Fire Alarm",
            "10-0-1", "10-0-2", "10-0-3", "10-0-4",
            "10-1-1", "10-1-2",
            "10-2-1", "10-2-2",
            "10-3-1", "10-3-2", "10-3-3", "10-3-5",
            "10-4-1", "10-4-2", "10-4-3",
            "10-5-1", "10-5-2", "10-5-3", "10-5-4",
            "10-6-1", "10-6-2",
            "10-7", "10-8",
            "Quartering level 1", "Quartering level 2", "Quartering level 3"
        ) else arrayOf(
            "Seleccione El Tipo De Emergencia",
            "1° Alarma de Incendio", "2° Alarma de Incendio", "3° Alarma de Incendio",
            "10-0-1", "10-0-2", "10-0-3", "10-0-4",
            "10-1-1", "10-1-2",
            "10-2-1", "10-2-2",
            "10-3-1", "10-3-2", "10-3-3", "10-3-5",
            "10-4-1", "10-4-2", "10-4-3",
            "10-5-1", "10-5-2", "10-5-3", "10-5-4",
            "10-6-1", "10-6-2",
            "10-7", "10-8",
            "Acuartelamiento grado 1°", "Acuartelamiento grado 2°", "Acuartelamiento grado 3°"
        )
        spinnerOpciones.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)

        // valida las opciones de unidad y si el dispositivo esta en ingles los muestra en ingles y si esta en español los muestra en español
        val unidades = if (idioma == "en")
            arrayOf("Select unit", "H-3", "BX-3", "B-3")
        else
            arrayOf("Seleccione unidad", "H-3", "BX-3", "B-3")
        spinnerUnidad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unidades)

        // valida las opciones de conductor y si el dispositivo esta en ingles los muestra en ingles y si esta en español los muestra en español
        val conductores = if (idioma == "en") arrayOf(
            "Select driver",
            "309 (Eusebio Madariaga)",
            "305 (Juan Carlos Toro)",
            "C-6 (Jimmy Salinas)",
            "C-7 (Jorge Nilo)",
            "337 (Antonio Hinojosa)"
        ) else arrayOf(
            "Seleccione conductor",
            "309 (Eusebio Madariaga)",
            "305 (Juan Carlos Toro)",
            "C-6 (Jimmy Salinas)",
            "C-7 (Jorge Nilo)",
            "337 (Antonio Hinojosa)"
        )
        spinnerConductores.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conductores)

        // campo de kilometros solo acpeta numeros si intenta escribir no lo va a dejar
        KmSalida.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        KmLlegada.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        KmSalida.keyListener = DigitsKeyListener.getInstance("0123456789")
        KmLlegada.keyListener = DigitsKeyListener.getInstance("0123456789")

        val digitsOnlyFilter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    val msg = if (idioma == "en") "Numbers only" else "Solo se aceptan números"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        }
        KmSalida.filters = arrayOf(digitsOnlyFilter)
        KmLlegada.filters = arrayOf(digitsOnlyFilter)

        //valida todos los campos y envia los datos si estan llenos
        btnEnviar.setOnClickListener {
            val obac = editObac.text.toString()
            val emergencia = spinnerOpciones.selectedItem.toString()
            val unidad = spinnerUnidad.selectedItem.toString()
            val direccion = editDireccion.text.toString()
            val conductor = spinnerConductores.selectedItem.toString()
            val kmSalida = KmSalida.text.toString()
            val kmLlegada = KmLlegada.text.toString()
            val bomberos = editBomberos.text.toString()
            val observaciones = editObservaciones.text.toString()

            val mensajeCompletar = if (idioma == "en") "Please complete all fields" else "Completa todos los campos"

            if (obac.isEmpty() || direccion.isEmpty() || bomberos.isEmpty() ||
                kmSalida.isEmpty() || kmLlegada.isEmpty() ||
                emergencia.contains("Seleccione") || unidad.contains("Seleccione") || conductor.contains("Seleccione") ||
                emergencia.contains("Select")) {
                Toast.makeText(this, mensajeCompletar, Toast.LENGTH_SHORT).show()
            } else {
                enviarDatos(
                    obac, emergencia, unidad, direccion,
                    conductor, kmSalida, kmLlegada, bomberos, observaciones
                )
            }
        }

        //borra todo lo que tienen los campos y los deja vacios para hacer la otra liosta
        btnNuevaLista.setOnClickListener {
            editObac.text.clear()
            editDireccion.text.clear()
            editBomberos.text.clear()
            editObservaciones.text.clear()
            KmSalida.text.clear()
            KmLlegada.text.clear()
            spinnerOpciones.setSelection(0)
            spinnerUnidad.setSelection(0)
            spinnerConductores.setSelection(0)

            val msg = if (idioma == "en") "Ready for a new record" else "Lista lista para un nuevo registro"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
       // muestra el mapa si escribiste alguna direccion
        val btnVerMapa = findViewById<Button>(R.id.button3)
        btnVerMapa.text = if (idioma == "en") "View Map" else "Ver Mapa"
        btnVerMapa.setOnClickListener {
            val direccion = editDireccion.text.toString()
            if (direccion.isNotEmpty()) {
                val mapaDialog = mapa(direccion)
                mapaDialog.show(supportFragmentManager, "Mapa")
            } else {
                val msg = if (idioma == "en") "Enter an address first" else "Ingresa una dirección primero"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }


    }

    // envía los datos del formu al servidor vía POST usando Volley
    private fun enviarDatos(
        obac: String, emergencia: String, unidad: String, direccion: String,
        conductor: String, kmSalida: String, kmLlegada: String,
        bomberos: String, observaciones: String
        
    ) {
        val url = "http://10.181.92.232/lista3era_app/insertar.php" //profe si lo prueba en su casa tiene q cambiarlo por su ip
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Servidor: $response", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(this, "Error al enviar: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["obac"] = obac
                params["emergencia"] = emergencia
                params["unidad"] = unidad
                params["direccion"] = direccion
                params["conductor"] = conductor
                params["km_salida"] = kmSalida
                params["km_llegada"] = kmLlegada
                params["bomberos"] = bomberos
                params["observaciones"] = observaciones
                val locale = Locale.getDefault().language
                params["idioma"] = locale
                return params
            }
        }
        queue.add(stringRequest)
    }
}


