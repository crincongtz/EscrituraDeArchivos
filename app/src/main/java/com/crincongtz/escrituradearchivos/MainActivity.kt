package com.crincongtz.escrituradearchivos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.crincongtz.escrituradearchivos.Constants.CONTENT_TYPE
import com.crincongtz.escrituradearchivos.Constants.EXT_TXT
import com.crincongtz.escrituradearchivos.Constants.FILE_PROVIDER
import java.io.*

class MainActivity : AppCompatActivity() {

    val TAG = "CursoKotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun generarTxt(view: View) {
        var nombreArchivo = "ArchivoDePrueba"

        var contenido = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"

        crearArchivoTxt(nombreArchivo, contenido)
        crearArchivoTxtExterno(nombreArchivo, contenido)
    }

    fun crearArchivoTxt(nombreArchivo: String, textoArchivo: String) {
        val directorio = this.filesDir

        val rutaDestino: String = directorio.absolutePath +
                File.separator + nombreArchivo + EXT_TXT
        Log.d(TAG, "El valor de la rutaDestino INTERNO es: $rutaDestino")

        val archivo = File(rutaDestino)
        archivo.setWritable(true)

        val escribirArchivo = FileWriter(archivo)
        escribirArchivo.append(textoArchivo) // Lorem ipsum
        escribirArchivo.close()

        Toast.makeText(this, "El archivo $nombreArchivo con el texto: $textoArchivo" +
                "fue creado de manera exitosa", Toast.LENGTH_LONG).show()
    }



    fun crearArchivoTxtExterno(nombreArchivo: String, textoArchivo: String) {
        val directorioRaiz = Environment.getExternalStorageDirectory().absolutePath
        val rutaDestino = directorioRaiz + File.separator + nombreArchivo + EXT_TXT
        Log.e(TAG, "El valor de la rutaDestino INTERNO es: $rutaDestino")

        val archivo = File(rutaDestino)
        try {

            val fileOutputStream = FileOutputStream(archivo)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.append(textoArchivo)
            outputStreamWriter.close()
            fileOutputStream.close()

        } catch (exception: FileNotFoundException) {

            exception.printStackTrace()
            Log.i(TAG, "*** File not found. Did you add a WRITE_EXTERNAL_STORAGE permission to the manifest")

        } catch (exception: IOException) {

            exception.printStackTrace()
        }


        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND

//        Log.d(TAG, "Authority: ${this.applicationContext.packageName + FILE_PROVIDER}")

        val uriFile: Uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + FILE_PROVIDER, archivo)
        sendIntent.putExtra(Intent.EXTRA_STREAM, uriFile)
        sendIntent.setType(CONTENT_TYPE)
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        sendIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        val shareIntent = Intent.createChooser(sendIntent, "Elige una app")
        startActivity(shareIntent)


    }



}
