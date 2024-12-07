package com.academiaaiapaec.appasistenciaaiapaec.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.academiaaiapaec.appasistenciaaiapaec.R
import com.academiaaiapaec.appasistenciaaiapaec.databinding.ActivityMainBinding
import com.academiaaiapaec.appasistenciaaiapaec.model.Asistencia
import com.academiaaiapaec.appasistenciaaiapaec.model.Estado
import com.academiaaiapaec.appasistenciaaiapaec.model.Sede
import com.academiaaiapaec.appasistenciaaiapaec.model.Turno
import com.academiaaiapaec.appasistenciaaiapaec.model.response.TrabajadorResponse
import com.academiaaiapaec.appasistenciaaiapaec.viewModel.AsistenciaViewModel
import com.academiaaiapaec.appasistenciaaiapaec.viewModel.EstadoViewModel
import com.academiaaiapaec.appasistenciaaiapaec.viewModel.SedeViewModel
import com.academiaaiapaec.appasistenciaaiapaec.viewModel.TrabajadorViewModel
import com.academiaaiapaec.appasistenciaaiapaec.viewModel.TurnoViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity: AppCompatActivity() {
    private val viewModelAsistencia: AsistenciaViewModel by viewModels()
    private val viewModelSede: SedeViewModel by viewModels()
    private val viewModelTurno:TurnoViewModel by viewModels()
    private val viewModelEstado:EstadoViewModel by viewModels()
    private val viewModelTrabjador:TrabajadorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var id_sede:Int=0
    private var id_turno:Int=0
    private var estado:String=""
    private var id_trabajador:Int=0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var mCurrentToast: Toast? = null
    private var currentSnackbar:Snackbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupTurnos()
        setupEstados()
        viewModelSede.getSede() // Cargar las sedes al iniciar
        viewModelTurno.getTurnos()
        viewModelEstado.getEstados()

        // Inicializamos el Handler
        handler = Handler(Looper.getMainLooper())

        // Runnable que se ejecutará cada segundo
        runnable = object : Runnable {
            override fun run() {
                updateDateTime() // Actualizar la hora
                handler.postDelayed(this, 1000) // Ejecutar nuevamente en 1 segundo
            }
        }

        // Iniciar la actualización del tiempo
        handler.post(runnable)
        binding.btnSubmit.setOnClickListener {
            saveAttendance()
        }
        // Configurar botón
        findViewById<Button>(R.id.btn_scan_qr).setOnClickListener {
            val integrator = IntentIntegrator(this)

            // Usar la actividad personalizada que creaste
            integrator.setCaptureActivity(CustomCaptureActivity::class.java)

            // Mantener la orientación bloqueada
            integrator.setOrientationLocked(true) // Bloquear orientación en vertical

            // Otras configuraciones
            integrator.setPrompt("Escanea el código QR")
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setCameraId(0) // Usar la cámara trasera

            // Iniciar el escaneo
            qrResultLauncher.launch( integrator.createScanIntent())

        }
    }
    private val qrResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val qrScanResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            val qrValue = qrScanResult?.contents

            if (qrValue != null) {
                try {
                    val trabajadorId = qrValue.toInt()  // Conversión explícita a entero
                    id_trabajador = trabajadorId
                    viewModelTrabjador.getTrabajador(id_trabajador)
                    loadTrabajador()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "El código QR no contiene un ID válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Escaneo no válido", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTrabajador() {
    viewModelTrabjador.trabajadorResult.observe(this,Observer{ result->
        result.onSuccess { response ->
            setupInputTrabajador(response)
        }.onFailure { error ->
            mostrarToastPersonalizado(this,"Error al obtener el trabajor: ${error.message}")

        }
    })
    }

    private fun setupInputTrabajador(response: TrabajadorResponse) {
        // Mapear los nombres DEL TRABAJADOR para mostrarlos en el input
        val nombre = response.data.nombre_trabajador +" "+ response.data.apellido_trabajador
        findViewById<TextView>(R.id.text_view_employee_name).text = nombre
    }

    private fun updateDateTime() {
        // Obtener la fecha y hora actual
        val calendar = Calendar.getInstance()

        // Día de la semana
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)

        // Hora, minutos y segundos
        val hour = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)
        // AM o PM
        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
        // Fecha en formato DD/MM/AAAA
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
        val formattedDate = dateFormat.format(calendar.time)
        // Actualizar los TextView
        findViewById<TextView>(R.id.text_hour).text = hour.toString().padStart(2, '0')
        findViewById<TextView>(R.id.text_minutes).text = minutes.toString().padStart(2, '0')
        findViewById<TextView>(R.id.text_seconds).text = seconds.toString().padStart(2, '0')

        // Día
        findViewById<TextView>(R.id.text_day).text = dayOfWeek.capitalize(Locale.getDefault())
        findViewById<TextView>(R.id.text_am_pm).text = amPm
        findViewById<TextView>(R.id.fecha).text = formattedDate
    }
    private fun setupTurnos(){
        viewModelTurno.turnoResult.observe(this,Observer {result ->
            result.onSuccess { response ->
                setupSpinnerTurnos(response) // Extrae los nombres
            }.onFailure { error ->
                mostrarToastPersonalizado(this,"Error al cargar los turnos: ${error.message}")

            }
        })
    }
    private fun setupEstados(){
        viewModelEstado.estadoResult.observe(this,Observer {result ->
            result.onSuccess { response ->
                setupSpinnerEstados(response) // Extrae los nombres
            }.onFailure { error ->
                Toast.makeText(this, "Error al cargar los turnos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setupObservers(){
        // Observa los cambios en la lista de sedes
        viewModelSede.sedeResult.observe(this, Observer { result ->
            result.onSuccess { response ->
                if (response.success) {
                    val sedes = response.data.data as? List<Sede> // Casting seguro
                    if (sedes != null) {
                        // Muestra las sedes en un Toast o las pasa al Spinner
                       // Toast.makeText(this, sedes.toString(), Toast.LENGTH_SHORT).show()
                        setupSpinnerSedes(sedes) // Extrae los nombres
                    } else {
                        //Toast.makeText(this, "No se pudo convertir los datos de sedes", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error del servidor: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                Toast.makeText(this, "Error al cargar las sedes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Observa el estado de carga
        viewModelSede.isLoading.observe(this, Observer { isLoading ->
            // Mostrar u ocultar un indicador de carga si es necesario
        })
    }
    private fun setupSpinnerTurnos(turnos: List<Turno>) {
        // Mapear los nombres de las sedes para mostrarlos en el Spinner
        val nombreTurno = turnos.map { it.nombre_turno }

        // Crear y asignar el adaptador
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombreTurno)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerShift.adapter = adapter

        // Listener para detectar la selección
        binding.spinnerShift.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtén el objeto Sede completo desde la posición
                val turnosSeleccionado = turnos[position]
                id_turno=turnosSeleccionado.id_turno

                // Mostrar información de la sede seleccionada
                /*
                Log.d("SedeSeleccionada", "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}")
                Toast.makeText(
                    this@MainActivity,
                    "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso donde no se selecciona nada (opcional)
            }
        }
    }
    private fun setupSpinnerSedes(sedes: List<Sede>) {
        // Mapear los nombres de las sedes para mostrarlos en el Spinner
        val nombresSedes = sedes.map { it.sede_nombre }

        // Crear y asignar el adaptador
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresSedes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLocation.adapter = adapter

        // Listener para detectar la selección
        binding.spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtén el objeto Sede completo desde la posición
                val sedeSeleccionada = sedes[position]
                id_sede=sedeSeleccionada.id_sede
                // Mostrar información de la sede seleccionada
                /*
                Log.d("SedeSeleccionada", "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}")
                Toast.makeText(
                    this@MainActivity,
                    "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso donde no se selecciona nada (opcional)
            }
        }
    }
    private fun  setupSpinnerEstados(estados : List<Estado>){
        // Mapear los nombres de las sedes para mostrarlos en el Spinner
        val nombreEstados = estados.map { it.nombre_estado}

        // Crear y asignar el adaptador
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombreEstados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = adapter

        // Listener para detectar la selección
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtén el objeto Sede completo desde la posición
                val estadoSeleccionada = estados[position]
                estado=estadoSeleccionada.nombre_estado
                // Mostrar información de la sede seleccionada
                /*
                Log.d("SedeSeleccionada", "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}")
                Toast.makeText(
                    this@MainActivity,
                    "ID: ${sedeSeleccionada.id_sede}, Nombre: ${sedeSeleccionada.sede_nombre}",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso donde no se selecciona nada (opcional)
            }
        }
    }
    private var fechaAsistencia: String? = null
    private var horaAsistencia: String? = null
    private fun saveAttendance() {
        if (id_sede == 0 || id_turno == 0 || id_trabajador == 0 || estado.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos antes de guardar.", Toast.LENGTH_SHORT).show()
            return
        }
        // Obtener la fecha y hora actual
        val calendar = Calendar.getInstance()
        // Formatear la fecha y la hora
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale("es", "ES"))

        fechaAsistencia = dateFormatter.format(calendar.time) // Ejemplo: 05/12/2024
        horaAsistencia = timeFormatter.format(calendar.time)  // Ejemplo: 03:45:12 PM
        val editTextRemarks = findViewById<EditText>(R.id.edit_remarks)
        // Obtener el texto que el usuario ha ingresado en el EditText
        val remarksText = editTextRemarks.text.toString()
        // Mostrar un mensaje al usuario
        //Toast.makeText(this, "Asistencia registrada: $fechaAsistencia $horaAsistencia", Toast.LENGTH_SHORT).show()
        val asistencia = Asistencia(
           id_trabajador= id_trabajador,
            fecha= fechaAsistencia.toString(),
             hora= horaAsistencia.toString(),
            id_turno= id_turno,
             id_sede= id_sede,
             tipo= estado,
             observacion= remarksText
        )
        // Llamar al método de registrar asistencia del ViewModel
       viewModelAsistencia.registrarAsistencia(asistencia)
        setupAsistencia()
        // Log para verificar
        Log.d("Asistencia AIAPAEC", asistencia.toString())
        //Log.d("Asistencia Data", "trabajador: $id_trabajador, sede: $id_sede, turno: $id_turno, estado: $estado,observacio,$remarksText")


    }
    private fun handleErrors(error: Throwable) {
        val errorMessage = error.message ?: "Error desconocido"
        mostrarSnackbar(this,errorMessage)

    }
    private fun setupAsistencia(){
        // Observar el resultado de la asistencia
        viewModelAsistencia.asistenciaResult.observe(this, Observer { result ->
            result.onSuccess { response ->
                // Aquí puedes manejar la respuesta exitosa
                mostrarSnackbar(this,"Asistencia registrada exitosamente")

            }.onFailure { error ->
                handleErrors(error)
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        // Detener el Handler cuando la actividad se destruye
        handler.removeCallbacks(runnable)
    }



    fun mostrarToastPersonalizado(context: Context, mensaje: String) {
        // Si hay un Toast activo, cancelarlo inmediatamente
        mCurrentToast?.cancel()

        // Inflar el diseño del Toast personalizado
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast_layout, null)

        // Obtener el TextView del diseño inflado y establecer el texto
        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = mensaje

        // Crear el nuevo Toast y asignarle el diseño inflado
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT  // O Toast.LENGTH_LONG, dependiendo de lo que necesites
        toast.view = layout  // Asignar la vista inflada al Toast

        // Almacenar el Toast actual para cancelarlo en el siguiente
        mCurrentToast = toast

        // Mostrar el Toast
        toast.show()

        // Eliminar el Toast anterior inmediatamente
        handler.postDelayed({
            mCurrentToast?.cancel()
            mCurrentToast = null  // Limpiar la referencia
        }, 200) // Reduce el tiempo de espera antes de eliminar el toast anterior (200ms)

    }
    fun mostrarSnackbar(context: Context, mensaje: String) {
        val rootView = (context as Activity).findViewById<View>(android.R.id.content)

        // Si hay un Snackbar activo, cancelarlo
        currentSnackbar?.dismiss()

        // Usar Handler para retrasar la creación del nuevo Snackbar
        Handler(mainLooper).postDelayed({
            // Crear y mostrar el nuevo Snackbar
            currentSnackbar = Snackbar.make(rootView, mensaje, Snackbar.LENGTH_SHORT)

            // Mostrar el nuevo Snackbar
            currentSnackbar?.show()

        }, 100)  // Retraso de 100ms para asegurar que el anterior haya desaparecido
    }


}